package org.example.researchassistant.domain;

public class Testz {
    private int Tid;
    private String Tag;
    private String title;
    private String description;
    private String img;
    private String url;

    public Testz() {
    }

    // 生成getter和setter方法
    public int getTid() {
        return Tid;
    }

    public void setTid(int Tid) {
        this.Tid = Tid;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String Tag) {
        this.Tag = Tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}