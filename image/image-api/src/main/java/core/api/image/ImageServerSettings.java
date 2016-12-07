package core.api.image;

/**
 * @author nick.guo
 */
public class ImageServerSettings {
    private String uploadServerUrl;
    private String serverUrl;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getUploadServerUrl() {
        return uploadServerUrl;
    }

    public void setUploadServerUrl(String uploadServerUrl) {
        this.uploadServerUrl = uploadServerUrl;
    }
}
