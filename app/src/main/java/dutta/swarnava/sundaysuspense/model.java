package dutta.swarnava.sundaysuspense;

import android.net.Uri;

public class model {
    private String img;
    private String title;
    private String author;
    private String url;

    model() {
    }

    public model(String img, String title, String author,String url) {
        this.img = img;
        this.title = title;
        this.author = author;
        this.url = url;
    }

    public void setimg(String img) {
        this.img = img;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public void setauthor(String author) {
        this.author = author;
    }

    public void seturl(String  url) {
        this.url = url;
    }

    public String getimg() {
        return img;
    }

    public String gettitle() {
        return title;
    }

    public String getauthor() {
        return author;
    }

    public String geturl() {
        return url;
    }
}
