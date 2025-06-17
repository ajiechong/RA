package org.example.researchassistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
//明确表名，不然会默认和实体类名一样
@TableName("history")
public class ChatHistory {
    // Getter和Setter方法
    @TableId(type = IdType.AUTO)
    private Integer  Hid;        // 对话ID，主键
    private Integer  Cid;        // 会话ID
    private Integer  Uid;        // 用户ID，外键关联user表
    private LocalDateTime Htime;    // 对话时间
    private String Content;     // 对话内容
    private String Hname;       // 会话名称，默认取对话内容前6个字符
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
        this.Hname = HName;
        this.Role = Role;
    }
    public void setHName(String HName) {
        this.Hname = Hname;
    }
    @Override
    public String toString() {
        return "ChatHistory{" +
                "Hid=" + Hid +
                ", Cid=" + Cid +
                ", Uid=" + Uid +
                ", Htime=" + Htime +
                ", Content='" + Content + '\'' +
                ", HName='" + Hname + '\'' +
                ", Role='" + Role + '\'' +
                '}';
    }
}
