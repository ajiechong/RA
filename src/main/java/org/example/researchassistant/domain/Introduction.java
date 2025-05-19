package org.example.researchassistant.domain;

public class Introduction {
    private int Iid;
    private String Type;
    private String Symptom;
    private String Causes;
    private String Treatment;
    private String Picture;

    public Introduction() {
    }

    // 生成getter和setter方法
    public int getIid() {
        return Iid;
    }

    public void setIid(int Iid) {
        this.Iid = Iid;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getSymptom() {
        return Symptom;
    }

    public void setSymptom(String Symptom) {
        this.Symptom = Symptom;
    }

    public String getCauses() {
        return Causes;
    }

    public void setCauses(String Causes) {
        this.Causes = Causes;
    }

    public String getTreatment() {
        return Treatment;
    }

    public void setTreatment(String Treatment) {
        this.Treatment = Treatment;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String Picture) {
        this.Picture = Picture;
    }
}