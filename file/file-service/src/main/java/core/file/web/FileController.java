package core.file.web;


import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import core.file.domain.FileMeta;
import core.file.service.FileAttributeService;
import core.file.service.FileMetaService;
import core.file.service.FileRepository;
import core.framework.database.FindResult;
import core.framework.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rainbow.cai
 */
@RestController
public class FileController {
    final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Inject
    FileMetaService fileMetaService;
    @Inject
    FileAttributeService fileAttributeService;
    @Inject
    FileRepository fileRepository;

    @RequestMapping(value = "/file/**", method = RequestMethod.GET)
    @ResponseBody
    public void file(HttpServletRequest request, HttpServletResponse response) {
        try {
            String path = StringUtils.substringAfter(request.getServletPath(), "/file");

            FileMeta fileMeta = fileMetaService.getByPath(request.getServletPath());
            if (fileMeta != null && fileMeta.getStatus() == 0) {
                throw new ResourceNotFoundException(path);
            }
            String fileExtension = Files.getFileExtension(path);
            //response.setContentType("application/octet-stream;charset=UTF-8"); //设置文件类型

            if (!Strings.isNullOrEmpty(fileExtension) && "pdf".equals(fileExtension.toLowerCase()))
                response.setContentType("application/pdf;charset=UTF-8");
            ByteStreams.copy(fileRepository.get(path, cookie(request.getCookies()), request.getParameter("token")), response.getOutputStream());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }


    private String cookie(Cookie[] cookies) {
        if (null == cookies) return "";
        for (Cookie cookie : cookies) {
            if ("PRIVATE_ID".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    @ResponseBody
    public FindResult<FileMeta> list(@RequestParam("offset") int offset, @RequestParam("fetchSize") int fetchSize) {
        List<FileMeta> fileEntities = fileMetaService.list(offset, fetchSize);
        if (fileEntities.isEmpty()) {
            fileEntities = new ArrayList<>();
        }
        return new FindResult<>(fileEntities, offset, fileMetaService.count());
    }

    @RequestMapping(value = "/file/search", method = RequestMethod.GET)
    @ResponseBody
    public FindResult<FileMeta> search(@RequestParam("search") String searchText, @RequestParam("offset") int offset, @RequestParam("fetchSize") int fetchSize) {
        List<FileMeta> fileEntities = fileMetaService.search("host", searchText, offset, fetchSize);
        if (fileEntities.isEmpty()) {
            fileEntities = new ArrayList<>();
        }
        return new FindResult<>(fileEntities, offset, fileMetaService.searchCount("host", searchText));
    }

    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, Object> saveOrUpdateFile(@RequestBody FileMeta fileMeta, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        FileMeta dbFileMeta = fileMetaService.getByPath(fileMeta.getPath());
        if (dbFileMeta == null) {
            result.put("status", "Failed. File not exits.Path=" + fileMeta.getPath());
            return result;
        }
        dbFileMeta.setHost(request.getRemoteHost());
        fileMetaService.update(dbFileMeta);
        fileAttributeService.update(dbFileMeta.getId(), fileMeta.getAttributes());
        result.put("status", "success");
        return result;
    }
}
