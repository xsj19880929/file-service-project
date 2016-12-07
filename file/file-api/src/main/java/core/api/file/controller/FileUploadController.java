package core.api.file.controller;

import core.api.file.FileServerSettings;
import core.api.file.client.FileUploadClient;
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
public class FileUploadController {
    @Inject
    FileUploadClient fileUploadClient;
    @Inject
    FileServerSettings fileSettings;

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @ResponseBody
    public FileUploadResponse upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        return fileUploadClient.upload(file, null != request.getParameter("tag") ? request.getParameter("tag") : "", Boolean.valueOf(request.getParameter("isPrivate")));
    }


    @RequestMapping(value = "/file/server", method = RequestMethod.GET)
    @ResponseBody
    public FileServerUrlResponse getFileDownloadUrl() {
        return new FileServerUrlResponse(fileSettings.getServerUrl());
    }
}
