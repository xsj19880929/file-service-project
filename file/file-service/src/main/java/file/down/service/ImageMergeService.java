package file.down.service;

import com.google.common.io.Files;
import core.file.service.FileRepository;
import core.framework.util.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linluxian on 15/8/23.
 */
@Component
public class ImageMergeService {

    @Inject
    private FileRepository fileRepository;

    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("haha".getBytes());
        byteArrayOutputStream.close();
        System.out.println(new String(byteArrayOutputStream.toByteArray()));
    }

    public byte[] mergeInVertical(List<String> imagePathList) {
        try {
            int outWidth = 0;
            int outHeight = 0;
            int[][] imageArrays = new int[imagePathList.size()][];
            List<BufferedImage> bufferedImageList = new ArrayList<>(imagePathList.size());
            for (int i = 0; i < imagePathList.size(); i++) {
                String imagePath = imagePathList.get(i);
                BufferedImage bufferedImage = ImageIO.read(fileRepository.get(StringUtils.substringAfter(imagePath, "file/")));
                imageArrays[i] = getImageArray(bufferedImage);

                outHeight += bufferedImage.getHeight();
                outWidth = Math.max(outWidth, bufferedImage.getWidth());
                bufferedImageList.add(bufferedImage);
            }
            //生成新图片
            BufferedImage outImage = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
            //设置新图片的RGB
            int heightOffset = 0;
            for (int i = 0; i < imageArrays.length; i++) {
                BufferedImage bufferedImage = bufferedImageList.get(i);
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();

                outImage.setRGB(0, heightOffset, width, height, imageArrays[i], 0, width);
                heightOffset += height;
            }
            String fileExtension = Files.getFileExtension(imagePathList.get(0));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(outImage, fileExtension, byteArrayOutputStream); //写图片
            byte[] buf = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 根据参数 垂直和水平方向 对2张图片做位移合并
     *
     * @return
     */
    public byte[] mergeByTwo(String imagePath1, String imagePath2, int x, int y) {
        try {
            BufferedImage bufferedImage1 = ImageIO.read(fileRepository.get(StringUtils.substringAfter(imagePath1, "file/")));
            BufferedImage bufferedImage2 = ImageIO.read(fileRepository.get(StringUtils.substringAfter(imagePath2, "file/")));
            int[] imageArray1 = getImageArray(bufferedImage1);
            int[] imageArray2 = getImageArray(bufferedImage2);

            //生成新图片
            int outWidth = bufferedImage1.getWidth() > (x + bufferedImage2.getWidth()) ? bufferedImage1.getWidth() : (x + bufferedImage2.getWidth());
            int outHeight = bufferedImage1.getHeight() > (y + bufferedImage2.getHeight()) ? bufferedImage1.getHeight() : (y + bufferedImage2.getHeight());
            BufferedImage outImage = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
            outImage.setRGB(0, 0, bufferedImage1.getWidth(), bufferedImage1.getHeight(), imageArray1, 0, bufferedImage1.getWidth());
            outImage.setRGB(x, y, bufferedImage2.getWidth(), bufferedImage2.getHeight(), imageArray2, 0, bufferedImage2.getWidth());

            // 输出新图片
            String fileExtension = Files.getFileExtension(imagePath1);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(outImage, fileExtension, byteArrayOutputStream); //写图片
            byte[] buf = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 根据参数 垂直和水平方向 对2张图片做位移合并
     *
     * @return
     */
    public byte[] mergeByTwo(InputStream in1, InputStream in2, int x, int y, String fileExtension) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            BufferedImage bufferedImage1 = ImageIO.read(in1);
            BufferedImage bufferedImage2 = ImageIO.read(in2);
            int[] imageArray1 = getImageArray(bufferedImage1);
            int[] imageArray2 = getImageArray(bufferedImage2);
            //生成新图片
            int outWidth = bufferedImage1.getWidth() > (x + bufferedImage2.getWidth()) ? bufferedImage1.getWidth() : (x + bufferedImage2.getWidth());
            int outHeight = bufferedImage1.getHeight() > (y + bufferedImage2.getHeight()) ? bufferedImage1.getHeight() : (y + bufferedImage2.getHeight());
            BufferedImage outImage = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
            outImage.setRGB(0, 0, bufferedImage1.getWidth(), bufferedImage1.getHeight(), imageArray1, 0, bufferedImage1.getWidth());
            outImage.setRGB(x, y, bufferedImage2.getWidth(), bufferedImage2.getHeight(), imageArray2, 0, bufferedImage2.getWidth());

            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(outImage, fileExtension, byteArrayOutputStream); //写图片
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(byteArrayOutputStream);
            IOUtils.close(in1);
            IOUtils.close(in2);
        }
        return new byte[0];
    }

    public byte[] mergeByTwoWithOrigin(InputStream in1, InputStream in2, int origin, int x, int y, String fileExtension) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            BufferedImage bufferedImage1 = ImageIO.read(in1);
            BufferedImage bufferedImage2 = ImageIO.read(in2);
            int[] imageArray1 = getImageArray(bufferedImage1);
            int[] imageArray2 = getImageArray(bufferedImage2);
            //生成新图片
            int widthDiff = x + bufferedImage2.getWidth() - bufferedImage1.getWidth();
            int heightDiff = y + bufferedImage2.getHeight() - bufferedImage1.getHeight();
            int outWidth = widthDiff > 0 ? (x + bufferedImage2.getWidth()) : bufferedImage1.getWidth();
            int outHeight = heightDiff > 0 ? (y + bufferedImage2.getHeight()) : bufferedImage1.getHeight();

            Coord coord = new Coord(widthDiff, heightDiff, x, y).reduce(origin);

            BufferedImage outImage = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
            outImage.setRGB(coord.getX1(), coord.getY1(), bufferedImage1.getWidth(), bufferedImage1.getHeight(), imageArray1, 0, bufferedImage1.getWidth());
            outImage.setRGB(coord.getX2(), coord.getY2(), bufferedImage2.getWidth(), bufferedImage2.getHeight(), imageArray2, 0, bufferedImage2.getWidth());

            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(outImage, fileExtension, byteArrayOutputStream); //写图片
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(byteArrayOutputStream);
            IOUtils.close(in1);
            IOUtils.close(in2);
        }
        return new byte[0];
    }

    // 图片转换成 整形数组
    private int[] getImageArray(BufferedImage bufferedImage) throws Exception {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[] intArray = new int[width * height];
        intArray = bufferedImage.getRGB(0, 0, width, height, intArray, 0, width);
        return intArray;
    }

    class Coord {
        private int x1;
        private int y1;
        private int x2;
        private int y2;

        private final int x;
        private final int y;
        private final int widthDiff;
        private final int heightDiff;

        public Coord(int widthDiff, int heightDiff, int x, int y) {
            this.widthDiff = widthDiff;
            this.heightDiff = heightDiff;
            this.x = x;
            this.y = y;
        }

        public Coord reduce(int origin) {
            switch (origin) {
                // 偏移的坐标原点在 图1的左上角
                case 0:
                    x1 = 0;
                    y1 = 0;
                    x2 = x;
                    y2 = y;
                    break;
                // 偏移的坐标原点在 图1的左下角
                case 1:
                    x1 = 0;
                    y1 = heightDiff > 0 ? heightDiff : 0;
                    x2 = x;
                    y2 = heightDiff > 0 ? 0 : (Math.abs(heightDiff));
                    break;
                // 偏移的坐标原点在 图1的右上角
                case 2:
                    x1 = widthDiff > 0 ? widthDiff : 0;
                    y1 = 0;
                    x2 = widthDiff > 0 ? 0 : (Math.abs(widthDiff));
                    y2 = y;
                    break;
                // 偏移的坐标原点在 图1的右下角
                case 3:
                    x1 = widthDiff > 0 ? widthDiff : 0;
                    y1 = heightDiff > 0 ? heightDiff : 0;
                    x2 = widthDiff > 0 ? 0 : (Math.abs(widthDiff));
                    y2 = heightDiff > 0 ? 0 : (Math.abs(heightDiff));
                    break;
                default:
                    throw new RuntimeException("unknow origin={" + origin + "}");
            }
            return this;
        }

        public int getX1() {
            return x1;
        }

        public int getY1() {
            return y1;
        }

        public int getX2() {
            return x2;
        }

        public int getY2() {
            return y2;
        }
    }

}
