package core.api.image.api;

/**
 * Created by lucian.lin on 15/12/16.
 */
public class ImageMergeByTwoWithOriginRequest {
    private String filePath1;
    private String filePath2;

    private int x;
    private int y;
    /**
     * 偏移坐标的 原点位置
     * 0: 左上角
     * 1: 左下角
     * 2: 右上角
     * 3: 右下角
     */
    private int origin;

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public String getFilePath1() {
        return filePath1;
    }

    public void setFilePath1(String filePath1) {
        this.filePath1 = filePath1;
    }

    public String getFilePath2() {
        return filePath2;
    }

    public void setFilePath2(String filePath2) {
        this.filePath2 = filePath2;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
