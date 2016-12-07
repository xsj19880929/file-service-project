package core.api.image.api;

import core.api.image.controller.ImageMergeByTwoRequest;
import core.api.image.controller.ImageMergeInVerticalRequest;
import core.api.image.controller.ImageMergeResponse;
import core.api.image.controller.ImageUploadResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author linluxian
 */
public interface ImageMergeAPIService {
    @RequestMapping(value = "/image-merge-by-two", method = RequestMethod.POST)
    @ResponseBody
    ImageMergeResponse imageMergeByTwo(@RequestBody ImageMergeByTwoRequest imageMergeByTwoRequest);

    @RequestMapping(value = "/image-merge-by-two-with-origin", method = RequestMethod.POST)
    @ResponseBody
    ImageMergeResponse imageMergeByTwoWithOrigin(@RequestBody ImageMergeByTwoWithOriginRequest imageMergeByTwoRequest);

    @RequestMapping(value = "/image-merge-in-vertical", method = RequestMethod.POST)
    @ResponseBody
    ImageMergeResponse imageMergeInVertical(@RequestBody ImageMergeInVerticalRequest imageMergeInVerticalRequest);

    @RequestMapping(value = "/image/url/upload", method = RequestMethod.GET)
    @ResponseBody
    ImageUploadResponse uploadUrl(@RequestParam("url") String url);
}
