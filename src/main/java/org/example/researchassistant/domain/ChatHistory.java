package org.example.researchassistant.domain;

import java.time.LocalDateTime;

public class ChatHistory {
    private int Hid;        // 对话ID，主键
    private int Cid;        // 会话ID
    private int Uid;        // 用户ID，外键关联user表
    private LocalDateTime Htime;    // 对话时间
    private String Content;     // 对话内容
    private String HName;       // 会话名称，默认取对话内容前6个字符
    private String Role;        // 角色标识(ai或user)

    // 构造函数
    public ChatHistory() {}

    public ChatHistory(int Hid, int Cid, int Uid, LocalDateTime Htime,
                       String Content, String HName, String Role) {
        this.Hid = Hid;
        this.Cid = Cid;
        this.Uid = Uid;
        this.Htime = Htime;
        this.Content = Content;
        this.HName = HName;
        this.Role = Role;
    }

    // Getter和Setter方法
    public int getHid() {
        return Hid;
    }

    public void setHid(int Hid) {
        this.Hid = Hid;
    }

    public int getCid() {
        return Cid;
    }

    public void setCid(int Cid) {
        this.Cid = Cid;
    }

    public int getUid() {
        return Uid;
    }

    public void setUid(int Uid) {
        this.Uid = Uid;
    }

    public LocalDateTime getHtime() {
        return Htime;
    }

    public void setHtime(LocalDateTime Htime) {
        this.Htime = Htime;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getHName() {
        return HName;
    }

    public void setHName(String HName) {
        this.HName = HName;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    @Override
    public String toString() {
        return "ChatHistory{" +
                "Hid=" + Hid +
                ", Cid=" + Cid +
                ", Uid=" + Uid +
                ", Htime=" + Htime +
                ", Content='" + Content + '\'' +
                ", HName='" + HName + '\'' +
                ", Role='" + Role + '\'' +
                '}';
    }
}