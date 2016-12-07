package core.api.image.controller;

import core.api.file.FileServerSettings;
import core.api.image.client.ImageUploadClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author nick.guo
 */
@Controller
public class ImageUploadController {
    @Inject
    ImageUploadClient imageUploadClient;
    @Inject
    FileServerSettings fileServerSettings;

    @RequestMapping(value = "/image/upload", method = RequestMethod.POST)
    @ResponseBody
    public ImageUploadResponse upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        return imageUploadClient.upload(file, null != request.getParameter("tag") ? request.getParameter("tag") : "", Boolean.valueOf(request.getParameter("isPrivate")));
    }


    @RequestMapping(value = "/image/server", method = RequestMethod.GET)
    @ResponseBody
    public ImageServerUrlResponse getFileDownloadUrl() {
        return new ImageServerUrlResponse(fileServerSettings.getImageUrl(), fileServerSettings.getImageUrl());
    }


}
