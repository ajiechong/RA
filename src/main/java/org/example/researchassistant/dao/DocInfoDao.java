package org.example.researchassistant.dao;

import org.example.researchassistant.domain.DocInfo;

import java.util.List;

public interface DocInfoDao {
    public List<DocInfo> getAllDocInfo();
    public List<DocInfo> getDocInfoByCondition(String condition);
    public void insertDocInfo(DocInfo docInfo);
    public void updateDocInfo(DocInfo docInfo, String condition);
    public void deleteDocInfo(String condition);

}
