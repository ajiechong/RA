package org.example.researchassistant.domain;

public class DocInfo {
    private int docId;
    private String docName;
    private String docSpecialties;
    private String docProfile;
    private String docImage;
    private String docHospital;
    private String hosUrl;
    private String title;
    private String spec_symptom;

    public DocInfo() {
    }
    public DocInfo(int docId, String docName, String docSpecialties, String docProfile, String docImage, String docHospital, String hosUrl,String title,String spec_symptom) {
        this.docId = docId;
        this.docName = docName;
        this.docSpecialties = docSpecialties;
        this.docProfile = docProfile;
        this.docImage = docImage;
        this.docHospital = docHospital;
        this.hosUrl = hosUrl;
        this.title=title;
        this.spec_symptom=spec_symptom;
    }
    // 以下是各个属性的getter和setter方法
    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocSpecialties() {
        return docSpecialties;
    }

    public void setDocSpecialties(String docSpecialties) {
        this.docSpecialties = docSpecialties;
    }

    public String getDocProfile() {
        return docProfile;
    }
    public String getTitle() {
        return title;
    }
    public void setDocProfile(String docProfile) {
        this.docProfile = docProfile;
    }

    public String getDocImage() {
        return docImage;
    }

    public void setDocImage(String docImage) {
        this.docImage = docImage;
    }

    public String getDocHospital() {
        return docHospital;
    }

    public void setDocHospital(String docHospital) {
        this.docHospital = docHospital;
    }

    public String getHosUrl() {
        return hosUrl;
    }

    public void setHosUrl(String hosUrl) {
        this.hosUrl = hosUrl;
    }
    public void setSpec_symptom(String spec_symptom){this.spec_symptom=spec_symptom;}
    public String getspec_symptom(){return this.spec_symptom;}
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return "DocInfo{" +
                "docId=" + docId +
                ", docName=" + docName +
                ", docSpecialties=" + docSpecialties +
                ", docProfile=" + docProfile +
                ", docImage=" + docImage +
                ", docHospital=" + docHospital +
                ", hosUrl=" + hosUrl +
                ",title="+title+
                ",spec_symptom="+spec_symptom+
                '}';
    }

}
