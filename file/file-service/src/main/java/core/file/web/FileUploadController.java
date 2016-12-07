package core.file.web;

import com.google.common.io.Files;
import core.file.domain.FileMeta;
import core.file.service.FileMetaService;
import core.file.service.FileRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * @author chi
 */
@RestController
public class FileUploadController {
    @Inject
    FileRepository fileRepository;

    @Inject
    FileMetaService fileMetaService;

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @ResponseBody
    public FileUploadResponse upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        Boolean isPrivate = Boolean.valueOf(request.getParameter("isPrivate"));
        String tag = request.getParameter("tag");
        String path = fileRepository.put(file.getInputStream(), tag, Files.getFileExtension(file.getOriginalFilename()), isPrivate);

        FileMeta fileMeta = new FileMeta();
        fileMeta.setPath("/file/" + path);
        CommonsMultipartFile commonsFile = (CommonsMultipartFile) file;
        fileMeta.setFileName(commonsFile.getFileItem().getName());
        fileMeta.setSize(commonsFile.getSize());
        fileMeta.setHost(request.getRemoteHost());
        fileMeta.setGroup(request.getParameter("group"));
        fileMeta.setIsPrivate(isPrivate);
        Date createTime = new Date();
        fileMeta.setCreateTime(createTime);
        fileMeta.setLastUpdateTime(createTime);
        fileMetaService.saveOrUpdate(fileMeta);

        return new FileUploadResponse(fileMeta.getPath());
    }


}
