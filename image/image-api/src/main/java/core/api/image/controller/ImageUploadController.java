package core.api.image.controller;

import core.api.image.ImageServerSettings;
import core.api.image.client.ImageUploadClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

/**
 * @author nick.guo
 */
@Controller
public class ImageUploadController {
    @Inject
    ImageUploadClient imageUploadClient;
    @Inject
    ImageServerSettings imageServerSettings;

    @RequestMapping(value = "/image/upload", method = RequestMethod.POST)
    @ResponseBody
    public ImageUploadResponse upload(@RequestParam("file") MultipartFile file) throws Exception {
        return imageUploadClient.upload(file);
    }


    @RequestMapping(value = "/image/server", method = RequestMethod.GET)
    @ResponseBody
    public ImageServerUrlResponse getFileDownloadUrl() {
        return new ImageServerUrlResponse(imageServerSettings.getServerUrl(), imageServerSettings.getUploadServerUrl());
    }


}
