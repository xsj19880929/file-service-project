package file.down.service;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import core.file.service.FileRepository;
import file.down.Images;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author nick.guo
 */
@Component
public class ImageRepository {
    public static final String PATH_SUFFIX = "/";
    public static final String MULTIPLY_SUFFIX = "x";
    static final Pattern PATTERN = Pattern.compile("^[0-9]+x[0-9]+$");


    @Inject
    ImageScalar imageScalar;
    @Inject
    FileRepository fileRepository;

    public byte[] image(String path) throws Exception {
        return image(path, true, false);
    }

    public byte[] image(String path, boolean crop, boolean raw) throws IOException {
        String hxw = path.substring(0, path.indexOf(PATH_SUFFIX));
        String[] hnw = hxw.split(MULTIPLY_SUFFIX);
        byte[] content = ByteStreams.toByteArray(fileRepository.get(StringUtils.substringAfter(path, "file/")));
        String format = Files.getFileExtension(path);
        if (StringUtils.isEmpty(format)) {
            format = Images.getFormat(new ByteArrayInputStream(content));
        }

        if (!PATTERN.matcher(hxw).find()) {
            return content;
        } else {
            return imageScalar.scale(content, format, Integer.parseInt(hnw[1]), Integer.parseInt(hnw[0]), crop);
        }
    }

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("^[0-9]+x[0-9]+$");
        System.out.println(pattern.matcher("240x240").find());
    }


}
