package org.example.researchassistant.domain;

public class Quiz {
    private int Qid;
    private String QName;
    private String Qtag;

    public Quiz() {
    }

    // 生成getter和setter方法
    public int getQid() {
        return Qid;
    }

    public void setQid(int Qid) {
        this.Qid = Qid;
    }

    public String getQName() {
        return QName;
    }

    public void setQName(String QName) {
        this.QName = QName;
    }

    public String getQtag() {
        return Qtag;
    }

    public void setQtag(String Qtag) {
        this.Qtag = Qtag;
    }
}