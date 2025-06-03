package org.example.researchassistant.domain;

import java.sql.Timestamp;

public class FavoriteChat {
    private Integer id;
    private Integer uid;
    private Integer cid;
    private Timestamp createTime;

    // 新增字段用于显示对话信息
    private String hname;  // 对话名称
    private Timestamp htime; // 对话时间

    // 构造函数
    public FavoriteChat() {}

    public FavoriteChat(Integer uid, Integer cid) {
        this.uid = uid;
        this.cid = cid;
    }

    public FavoriteChat(Integer uid, Integer cid, String hname) {
        this.uid = uid;
        this.cid = cid;
        this.hname = hname;
    }

    // 原有的 getter 和 setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    // 新增的 getter 和 setter
    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public Timestamp getHtime() {
        return htime;
    }

    public void setHtime(Timestamp htime) {
        this.htime = htime;
    }

    @Override
    public String toString() {
        return "FavoriteChat{" +
                "id=" + id +
                ", uid=" + uid +
                ", cid=" + cid +
                ", createTime=" + createTime +
                ", hname='" + hname + '\'' +
                ", htime=" + htime +
                '}';
    }
}