package file.down.web;

import com.google.common.io.Files;
import core.api.image.api.ImageMergeAPIService;
import core.api.image.api.ImageMergeByTwoWithOriginRequest;
import core.api.image.client.ImageUploadClient;
import core.api.image.controller.ImageMergeByTwoRequest;
import core.api.image.controller.ImageMergeInVerticalRequest;
import core.api.image.controller.ImageMergeResponse;
import core.api.image.controller.ImageUploadResponse;
import file.down.service.ImageMergeService;
import file.down.service.ImageRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author lu.cain
 */
@Controller
public class ImageMergeController implements ImageMergeAPIService {
    final Logger logger = LoggerFactory.getLogger(ImageMergeController.class);


    @Inject
    ImageRepository imageRepository;

    @Inject
    ImageMergeService imageMergeService;

    @Inject
    ImageUploadClient imageUploadClient;

    @Override
    public ImageMergeResponse imageMergeByTwo(@RequestBody ImageMergeByTwoRequest imageMergeByTwoRequest) {
        ImageMergeResponse imageMergeResponse = new ImageMergeResponse();
        try {
            //String filePath1 = imageMergeByTwoRequest.getFilePath1();
            //String filePath2 = imageMergeByTwoRequest.getFilePath2();
            int x = imageMergeByTwoRequest.getX();
            int y = imageMergeByTwoRequest.getY();
            String fileExtension = Files.getFileExtension(imageMergeByTwoRequest.getFilePath1());
            fileExtension = removeParam(fileExtension);
            byte[] outImge = imageMergeService.mergeByTwo(imageUploadClient.down(imageMergeByTwoRequest.getFilePath1()), imageUploadClient.down(imageMergeByTwoRequest.getFilePath2()), x, y, fileExtension);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outImge);
            ImageUploadResponse imageUploadResponse = imageUploadClient.upload(byteArrayInputStream, "imageMerge." + fileExtension, "imageMergeGroup");

            imageMergeResponse.setPath(imageUploadResponse.getPath());
            return imageMergeResponse;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return imageMergeResponse;
    }

    @Override
    public ImageMergeResponse imageMergeByTwoWithOrigin(@RequestBody ImageMergeByTwoWithOriginRequest imageMergeByTwoWithOriginRequest) {
        ImageMergeResponse imageMergeResponse = new ImageMergeResponse();
        try {
            int x = imageMergeByTwoWithOriginRequest.getX();
            int y = imageMergeByTwoWithOriginRequest.getY();
            int origin = imageMergeByTwoWithOriginRequest.getOrigin();

            String fileExtension = Files.getFileExtension(imageMergeByTwoWithOriginRequest.getFilePath1());
            fileExtension = removeParam(fileExtension);

            InputStream inputStream1 = imageUploadClient.down(imageMergeByTwoWithOriginRequest.getFilePath1());
            InputStream inputStream2 = imageUploadClient.down(imageMergeByTwoWithOriginRequest.getFilePath2());

            byte[] outImge = imageMergeService.mergeByTwoWithOrigin(inputStream1, inputStream2, origin, x, y, fileExtension);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outImge);
            ImageUploadResponse imageUploadResponse = imageUploadClient.upload(byteArrayInputStream, "imageMerge." + fileExtension, "imageMergeGroup");

            imageMergeResponse.setPath(imageUploadResponse.getPath());
            return imageMergeResponse;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return imageMergeResponse;
    }


    @Override
    public ImageMergeResponse imageMergeInVertical(@RequestBody ImageMergeInVerticalRequest imageMergeInVerticalRequest) {
        ImageMergeResponse imageMergeResponse = new ImageMergeResponse();

        try {
            List<String> filePathList = imageMergeInVerticalRequest.getFilePathList();
            byte[] outImge = imageMergeService.mergeInVertical(filePathList);

            String fileExtension = Files.getFileExtension(filePathList.get(0));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outImge);
            ImageUploadResponse imageUploadResponse = imageUploadClient.upload(byteArrayInputStream, "imageMerge." + fileExtension, "imageMergeGroup");

            imageMergeResponse.setPath(imageUploadResponse.getPath());
            return imageMergeResponse;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return imageMergeResponse;
    }

    @Override
    public ImageUploadResponse uploadUrl(@RequestParam("url") String url) {
        try {
            return imageUploadClient.upload(imageUploadClient.down(url), removeParam(url), "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private String removeParam(String url) {
        if (StringUtils.isNotEmpty(url)) {
            return url.split("\\?")[0];
        }
        return "";
    }
}
