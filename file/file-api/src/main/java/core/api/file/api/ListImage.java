package core.api.file.api;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author nick.guo
 */
public class ListImage {
    private String state;
    private List<Image> list = Lists.newArrayList();
    private int start;
    private int total;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void addPath(String path) {
        list.add(new Image(path));
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Image> getList() {
        return list;
    }

    public void setList(List<Image> list) {
        this.list = list;
    }

    public static class Image {
        private String url;

        public Image(String url) {
            this.url = url;
        }

        public Image() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
