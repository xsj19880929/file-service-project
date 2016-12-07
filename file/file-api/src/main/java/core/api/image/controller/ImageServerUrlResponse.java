package core.api.image.controller;

/**
 * @author rainbow.cai
 */
public class ImageServerUrlResponse {
    private final String url;
    private final String uploadUrl;

    public ImageServerUrlResponse(String url, String uploadUrl) {
        this.url = url;
        this.uploadUrl = uploadUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }
}
