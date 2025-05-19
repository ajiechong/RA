package DatabaseTest;

import org.example.researchassistant.dao.impl.QuizDaoImpl;
import org.example.researchassistant.domain.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizDaoImplTest {

    private QuizDaoImpl quizDaoImpl;

    @BeforeEach
    public void setUp() {
        quizDaoImpl = new QuizDaoImpl();
    }

    @Test
    public void testGetAllQuizzes() {
        List<Quiz> quizzes = quizDaoImpl.getAllQuizzes();
        assertNotNull(quizzes);
    }

    @Test
    public void testGetQuizById() {
        // 假设数据库中存在一个测验记录，其qid为1（可根据实际情况调整）
        int qid = 1;
        Quiz quiz = quizDaoImpl.getQuizById(qid);
        if (quiz!= null) {
            assertEquals(qid, quiz.getQid());
        } else {
            assertNull(quiz);
        }
    }

    @Test
    public void testGetQuizzesByName() {
        // 假设数据库中存在名称为"SomeQuizName"的相关测验记录（可根据实际情况调整）
        String qName = "SomeQuizName";
        List<Quiz> quizzes = quizDaoImpl.getQuizzesByName(qName);
        assertNotNull(quizzes);
    }

    @Test
    public void testGetQuizzesByTag() {
        // 假设数据库中存在标签为"SomeQuizTag"的相关测验记录（可根据实际情况调整）
        String qtag = "SomeQuizTag";
        List<Quiz> quizzes = quizDaoImpl.getQuizzesByTag(qtag);
        assertNotNull(quizzes);
    }

    @Test
    public void testInsertQuiz() {
        Quiz quiz = new Quiz();
        quiz.setQName("NewQuizName");
        quiz.setQtag("NewQuizTag");

        quizDaoImpl.insertQuiz(quiz);

        // 再次查询数据库，验证是否插入成功
        List<Quiz> quizzes = quizDaoImpl.getQuizzesByName(quiz.getQName());
        assertTrue(quizzes.stream().anyMatch(q -> q.getQtag().equals(quiz.getQtag())));
    }

    @Test
    public void testUpdateQuiz() {
        // 假设数据库中存在一个测验记录，其qid为2（可根据实际情况调整）
        int qid = 2;
        Quiz originalQuiz = quizDaoImpl.getQuizById(qid);

        if (originalQuiz!= null) {
            originalQuiz.setQtag("UpdatedQuizTag");
            quizDaoImpl.updateQuiz(originalQuiz, "qid = " + qid);

            Quiz updatedQuiz = quizDaoImpl.getQuizById(qid);
            assertEquals("UpdatedQuizTag", updatedQuiz.getQtag());
        } else {
            assertNull(originalQuiz);
        }
    }

    @Test
    public void testDeleteQuiz() {
        // 假设数据库中存在一个测验记录，其qid为3（可从实际情况调整）
        int qid = 3;
        Quiz quizToDelete = quizDaoImpl.getQuizById(qid);

        if (quizToDelete!= null) {
            quizDaoImpl.deleteQuiz("qid = " + qid);

            Quiz deletedQuiz = quizDaoImpl.getQuizById(qid);
            assertNull(deletedQuiz);
        } else {
            assertNull(quizToDelete);
        }
    }
}