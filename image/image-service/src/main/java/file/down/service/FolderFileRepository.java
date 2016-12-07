package file.down.service;

import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author chi
 */
public class FolderFileRepository implements FileRepository {
    final Logger logger = LoggerFactory.getLogger(FolderFileRepository.class);
    private final File dir;

    public FolderFileRepository(File dir) {
        this.dir = dir;
    }

    @Override
    public InputStream get(String path) {
        File file = new File(dir, path);
        logger.debug("dir={},path={}", dir, path);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                logger.warn("File not found.");
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public String put(InputStream inputStream, String fileExtension) {
        try {
            byte[] content = ByteStreams.toByteArray(inputStream);
            String path = Hashing.md5().hashBytes(content).toString() + '.' + fileExtension;
            File file = new File(dir, path);
            com.google.common.io.Files.createParentDirs(file);
            com.google.common.io.Files.write(content, file);
            return path;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}