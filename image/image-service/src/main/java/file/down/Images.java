package file.down;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author nick.guo
 */
public class Images {
    public static String getFormat(InputStream in) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(in);

        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
            return null;
        }

        ImageReader reader = iter.next();

        iis.close();

        return reader.getFormatName();

    }


}
