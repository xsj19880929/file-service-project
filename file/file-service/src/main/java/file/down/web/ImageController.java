package file.down.web;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import file.down.Images;
import file.down.service.ImageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.Date;

/**
 * @author nick.guo
 */
@Controller
public class ImageController {

    @Inject
    ImageRepository imageRepository;

    @RequestMapping(value = "/image/**", method = RequestMethod.GET)
    @ResponseBody
    public void image(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        long ifModifiedSinceTime = httpServletRequest.getDateHeader("If-Modified-Since");
        if (ifModifiedSinceTime > 0) {
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
            return;
        }
        try {
            boolean crop = httpServletRequest.getParameter("crop") == null || Boolean.valueOf(httpServletRequest.getParameter("crop"));
            boolean raw = httpServletRequest.getParameter("raw") != null && Boolean.valueOf(httpServletRequest.getParameter("raw"));

            String filePath = StringUtils.substringAfter(httpServletRequest.getServletPath(), "/image/");
            byte[] image = imageRepository.image(filePath, crop, raw);

            String fileExtension = Files.getFileExtension(filePath);
            if (Strings.isNullOrEmpty(fileExtension)) {
                fileExtension = Images.getFormat(new ByteArrayInputStream(image));
            }
            if (Strings.isNullOrEmpty(fileExtension)) {
                fileExtension = "*";
            }
            response.setContentType("image/" + fileExtension + ";charset=UTF-8");
            Date date = new Date();
            response.addHeader("Cache-Control", "max-age=2592000");
            response.setDateHeader("Last-Modified", date.getTime());
            response.setHeader("Pragma", "Pragma");
            response.getOutputStream().write(image);
        } catch (Exception e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }
}
