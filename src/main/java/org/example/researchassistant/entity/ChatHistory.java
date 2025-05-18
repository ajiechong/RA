package org.example.researchassistant.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatHistory {
    // Getter和Setter方法
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