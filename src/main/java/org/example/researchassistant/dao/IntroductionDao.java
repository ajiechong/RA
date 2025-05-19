package org.example.researchassistant.dao;

import org.example.researchassistant.domain.Introduction;
import java.util.List;

public interface IntroductionDao {
    // 获取所有Introduction记录
    List<Introduction> getAllIntroductions();

    // 根据Introduction ID获取特定记录
    Introduction getIntroductionById(int iid);

    // 根据症状名称获取相关记录
    List<Introduction> getIntroductionsByType(String type);

    // 根据症状名称进行模糊查询
    List<Introduction> searchIntroductionsBySymptom(String symptomName);

    // 插入新的Introduction记录
    void insertIntroduction(Introduction introduction);

    // 更新Introduction记录
    void updateIntroduction(Introduction introduction, String condition);

    // 删除Introduction记录
    void deleteIntroduction(String condition);
}