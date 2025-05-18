package DatabaseTest;

import java.util.List;

public class TestDocInfo {
    public static void main(String[] args) {
        DocInfoDaoImpl docInfoDao = new DocInfoDaoImpl();
        // find all doctor info
        List<DocInfo> allDocInfo = docInfoDao.getAllDocInfo();
        System.out.println("all doctor info:");
        for (DocInfo docInfo : allDocInfo) {
            System.out.println(docInfo);
        }

        // search for doctor info by condition
        List<DocInfo> filteredDocInfo = docInfoDao.getDocInfoByCondition("doc_name = '陈医生'");
        System.out.println("Doctor chen:");
        for (DocInfo docInfo : filteredDocInfo) {
            System.out.println(docInfo);
        }

        // insert doctor info
        DocInfo newDocInfo = new DocInfo();
        newDocInfo.setDocName("new Doctor");
        newDocInfo.setDocSpecialties("new Specialties");
        newDocInfo.setDocProfile("new introduction");
        newDocInfo.setDocImage("new photo path");
        newDocInfo.setDocHospital("new hospital");
        newDocInfo.setHosUrl("new hospital url");
        docInfoDao.insertDocInfo(newDocInfo);

        // update doctor info
        DocInfo updateDocInfo = new DocInfo();
        updateDocInfo.setDocName("update doctor");
        updateDocInfo.setDocSpecialties("update specialties");
        updateDocInfo.setDocProfile("update introduction");
        updateDocInfo.setDocImage("update photo path");
        updateDocInfo.setDocHospital("update hospital");
        updateDocInfo.setHosUrl("update hospital url");
        docInfoDao.updateDocInfo(updateDocInfo, "doc_name = '新医生'");

        // delete doctor info
        docInfoDao.deleteDocInfo("doc_name = '新医生'");
    }
}