package DatabaseTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntroductionDaoImplTest {

    private IntroductionDaoImpl introductionDaoImpl;

    @BeforeEach
    public void setUp() {
        introductionDaoImpl = new IntroductionDaoImpl();
    }

    @Test
    public void testGetAllIntroductions() {
        List<Introduction> introductions = introductionDaoImpl.getAllIntroductions();
        assertNotNull(introductions);
        assertFalse(introductions.isEmpty());
    }

    @Test
    public void testGetIntroductionById() {
        // 先插入一条测试数据
        Introduction testIntro = new Introduction();
        testIntro.setType("TestType");
        testIntro.setSymptom("TestSymptom");
        testIntro.setCauses("TestCauses");
        testIntro.setTreatment("TestTreatment");
        testIntro.setPicture("TestPicture");

        introductionDaoImpl.insertIntroduction(testIntro);

        // 获取刚插入的数据的ID
        List<Introduction> allIntros = introductionDaoImpl.getAllIntroductions();
        int lastId = allIntros.get(allIntros.size() - 1).getIid();

        Introduction introduction = introductionDaoImpl.getIntroductionById(lastId);
        assertNotNull(introduction);
        assertEquals("TestType", introduction.getType());

        // 清理测试数据
        introductionDaoImpl.deleteIntroduction("Iid = " + lastId);
    }

    @Test
    public void testGetIntroductionsByType() {
        // 插入测试数据
        Introduction testIntro = new Introduction();
        testIntro.setType("SpecificTestType");
        testIntro.setSymptom("TestSymptom");
        testIntro.setCauses("TestCauses");
        testIntro.setTreatment("TestTreatment");
        testIntro.setPicture("TestPicture");

        introductionDaoImpl.insertIntroduction(testIntro);

        List<Introduction> introductions = introductionDaoImpl.getIntroductionsByType("SpecificTestType");
        assertNotNull(introductions);
        assertFalse(introductions.isEmpty());
        assertEquals("SpecificTestType", introductions.get(0).getType());

        // 清理测试数据
        introductionDaoImpl.deleteIntroduction("Type = 'SpecificTestType'");
    }

    @Test
    public void testInsertAndUpdateIntroduction() {
        // 测试插入
        Introduction introduction = new Introduction();
        introduction.setType("InsertType");
        introduction.setSymptom("InsertSymptom");
        introduction.setCauses("InsertCauses");
        introduction.setTreatment("InsertTreatment");
        introduction.setPicture("InsertPicture");

        introductionDaoImpl.insertIntroduction(introduction);

        // 获取插入的数据
        List<Introduction> inserted = introductionDaoImpl.getIntroductionsByType("InsertType");
        assertFalse(inserted.isEmpty());
        int insertedId = inserted.get(0).getIid();

        // 测试更新
        introduction.setSymptom("UpdatedSymptom");
        introductionDaoImpl.updateIntroduction(introduction, "Iid = " + insertedId);

        Introduction updated = introductionDaoImpl.getIntroductionById(insertedId);
        assertEquals("UpdatedSymptom", updated.getSymptom());

        // 清理测试数据
        introductionDaoImpl.deleteIntroduction("Iid = " + insertedId);
    }

    @Test
    public void testDeleteIntroduction() {
        // 插入测试数据
        Introduction testIntro = new Introduction();
        testIntro.setType("DeleteTestType");
        testIntro.setSymptom("DeleteTestSymptom");
        testIntro.setCauses("DeleteTestCauses");
        testIntro.setTreatment("DeleteTestTreatment");
        testIntro.setPicture("DeleteTestPicture");

        introductionDaoImpl.insertIntroduction(testIntro);

        // 获取插入的数据的ID
        List<Introduction> allIntros = introductionDaoImpl.getAllIntroductions();
        int lastId = allIntros.get(allIntros.size() - 1).getIid();

        // 删除数据
        introductionDaoImpl.deleteIntroduction("Iid = " + lastId);

        // 验证删除
        List<Introduction> afterDelete = introductionDaoImpl.getIntroductionsByType("DeleteTestType");
        assertTrue(afterDelete.isEmpty());
    }
}