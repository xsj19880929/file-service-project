package core.file.web;

/**
 * @author chi
 */
public class FileUploadResponse {
    private final String path;

    public FileUploadResponse(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
