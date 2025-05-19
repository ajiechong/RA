package org.example.researchassistant.domain;

public class ResourceVideo {
    private int Rid;
    private String Rname;
    private String RTime;
    private String Rurl;
    private int Rstore;
    private String Rtag;
    private String Rabstract;
    private String rimg;  // 新增的字段，用于存储资源的图片路径

    public ResourceVideo() {
    }

    // 生成getter和setter方法
    public int getRid() {
        return Rid;
    }

    public void setRid(int Rid) {
        this.Rid = Rid;
    }

    public String getRname() {
        return Rname;
    }

    public void setRname(String Rname) {
        this.Rname = Rname;
    }

    public String getRTime() {
        return RTime;
    }

    public void setRTime(String RTime) {
        this.RTime = RTime;
    }

    public String getRurl() {
        return Rurl;
    }

    public void setRurl(String Rurl) {
        this.Rurl = Rurl;
    }

    public int getRstore() {
        return Rstore;
    }

    public void setRstore(int Rstore) {
        this.Rstore = Rstore;
    }

    public String getRtag() {
        return Rtag;
    }

    public void setRtag(String Rtag) {
        this.Rtag = Rtag;
    }

    public String getRabstract() {
        return Rabstract;
    }

    public void setRabstract(String Rabstract) {
        this.Rabstract = Rabstract;
    }

    public String getRimg() {return rimg;}

    public void setRimg(String Rimg) {this.rimg = Rimg;}
}