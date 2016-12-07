package file.down.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author rainbow.cai
 */
public class ImageScalarTest {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 1; i++) {
            ImageScalar imageScalar = new ImageScalar();
            String fileString = "/Users/chi/Workspace/gitlab.yuntu.com/file-service-project/image/image-service/src/test/resources/635d107181bcc8060ecabc3af43c879c.jpeg";
            Files.write(Paths.get("/Users/chi/Workspace/gitlab.yuntu.com/file-service-project/image/image-service/src/test/resources/635d107181bcc8060ecabc3af43c879c.100x100.jpeg"),
                    imageScalar.scale(Files.readAllBytes(Paths.get(fileString)), "jpg", 100, 100, false));

        }
    }
}