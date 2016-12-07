package file.down.service;

import org.imgscalr.Scalr;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageScalar {
    public byte[] scale(byte[] content, String imageType, int height, int width) {
        return scale(content, imageType, height, width, true);
    }

    public byte[] scale(byte[] content, String imageType, int height, int width, boolean crop) {
        try {
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(content));
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            BufferedImage result;
            if (crop) {
                BufferedImage chopped = crop(src, height, width);
                result = Scalr.resize(chopped, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, width, height);
            } else {
                boolean cropY = isCropY(src.getHeight(), src.getWidth(), height, width);

                if (cropY) {
                    result = Scalr.resize(src, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, height * src.getWidth() / src.getHeight(), height);
                } else {
                    result = Scalr.resize(src, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, width, width * src.getHeight() / src.getWidth());
                }
            }

            if ("jpg".equals(imageType) || "jpeg".equals(imageType)) {
                ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.9F);
                writer.setOutput(ImageIO.createImageOutputStream(out));
                writer.write(null, new IIOImage(result, null, null), param);
            } else {
                ImageIO.write(result, imageType, out);
            }

            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    final BufferedImage crop(BufferedImage src, int height, int width) {
        if (isCropY(src.getHeight(), src.getWidth(), height, width)) {
            double rate = (double) height / width;
            int y = (int) (src.getWidth() * rate);
            return Scalr.crop(src, 0, (src.getHeight() - y) / 2, src.getWidth(), y);
        } else {
            double rate = (double) width / height;
            int x = (int) (src.getHeight() * rate);
            return Scalr.crop(src, (src.getWidth() - x) / 2, 0, x, src.getHeight());
        }
    }

    final boolean isCropY(int height, int width, int targetHeight, int targetWidth) {
        double srcRate = (double) height / width;
        double targetRate = (double) targetHeight / targetWidth;
        return srcRate > targetRate;
    }
}