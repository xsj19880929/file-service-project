package core.api.image.controller;

import java.util.List;

/**
 * @author linluxian
 */
public class ImageMergeInVerticalRequest {
    List<String> filePathList;

    public List<String> getFilePathList() {
        return filePathList;
    }

    public void setFilePathList(List<String> filePathList) {
        this.filePathList = filePathList;
    }


}
