package file.down.service;

import java.io.InputStream;

/**
 * @author chi
 */
public interface FileRepository {
    InputStream get(String path);

    String put(InputStream inputStream, String fileExtension);
}