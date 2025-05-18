
package DatabaseTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class TestDaoImplTest {

    private TestzDao testzDao;

    @BeforeEach
    public void setUp() {
        // Mock TestzDao接口
        testzDao = mock(TestzDao.class);
    }

    @Test
    public void testGetAllTests() {
        // 准备测试数据
        Testz test1 = new Testz();
        test1.setTid(1);
        test1.setTitle("Test 1");

        Testz test2 = new Testz();
        test2.setTid(2);
        test2.setTitle("Test 2");

        List<Testz> expectedTests = Arrays.asList(test1, test2);

        // 模拟TestzDao的行为
        when(testzDao.getAllTests()).thenReturn(expectedTests);

        // 调用方法并验证结果
        List<Testz> result = testzDao.getAllTests();
        assertEquals(2, result.size());
        assertEquals("Test 1", result.get(0).getTitle());
    }

    @Test
    public void testGetTestById() {
        // 准备测试数据
        Testz test = new Testz();
        test.setTid(1);
        test.setTitle("Test 1");

        // 模拟TestzDao的行为
        when(testzDao.getTestById(1)).thenReturn(test);

        // 调用方法并验证结果
        Testz result = testzDao.getTestById(1);
        assertNotNull(result);
        assertEquals(1, result.getTid());
        assertEquals("Test 1", result.getTitle());
    }

    @Test
    public void testGetTestsByTag() {
        // 准备测试数据
        Testz test1 = new Testz();
        test1.setTid(1);
        test1.setTag("Health");

        Testz test2 = new Testz();
        test2.setTid(2);
        test2.setTag("Health");

        List<Testz> expectedTests = Arrays.asList(test1, test2);

        // 模拟TestzDao的行为
        when(testzDao.getTestsByTag("Health")).thenReturn(expectedTests);

        // 调用方法并验证结果
        List<Testz> result = testzDao.getTestsByTag("Health");
        assertEquals(2, result.size());
        assertEquals("Health", result.get(0).getTag());
    }

    @Test
    public void testInsertTest() {
        // 准备测试数据
        Testz test = new Testz();
        test.setTid(1);
        test.setTitle("Test Insert");

        // 执行插入方法，验证插入行为
        doNothing().when(testzDao).insertTest(test);
        testzDao.insertTest(test);
        verify(testzDao, times(1)).insertTest(test);
    }

    @Test
    public void testUpdateTest() {
        // 准备测试数据
        Testz test = new Testz();
        test.setTid(1);
        test.setTitle("Test Update");

        // 执行更新方法
        doNothing().when(testzDao).updateTest(test, "Tid = 1");
        testzDao.updateTest(test, "Tid = 1");
        verify(testzDao, times(1)).updateTest(test, "Tid = 1");
    }

    @Test
    public void testDeleteTest() {
        // 执行删除方法
        doNothing().when(testzDao).deleteTest("Tid = 1");
        testzDao.deleteTest("Tid = 1");
        verify(testzDao, times(1)).deleteTest("Tid = 1");
    }
}
