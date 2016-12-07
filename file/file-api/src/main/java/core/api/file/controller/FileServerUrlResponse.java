package core.api.file.controller;

/**
 * @author rainbow.cai
 */
public class FileServerUrlResponse {
    private final String url;

    public FileServerUrlResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
