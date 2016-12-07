package core.file.service;

import java.io.InputStream;

/**
 * @author chi
 */
public interface FileRepository {
    InputStream get(String path);

    String put(InputStream inputStream, String fileExtension);

    String put(InputStream inputStream, String fileExtension, Boolean isPrivate);

    String put(InputStream inputStream, String tag, String fileExtension, Boolean isPrivate);

    InputStream get(String path, String cookie, String token);
}