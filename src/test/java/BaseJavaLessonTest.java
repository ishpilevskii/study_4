import inno.tech.study.BaseJavaLesson;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BaseJavaLessonTest {

    private static final Random RANDOM = new Random();

    @BeforeEach
    void printStart() {
        System.out.println("========================");
        System.out.println("Test method start");
    }

    @AfterEach
    void printEnd() {
        System.out.println("Test method end");
        System.out.println("========================");
    }

    @Test
    void isEvenTest() {
        int n = RANDOM.nextInt(1, 101);
        assertEquals(n % 2 == 0, BaseJavaLesson.isEven(n));
    }

    @RepeatedTest(20)
    void checkAccessTest() {
        int age = RANDOM.nextInt(0, 100);
        String expected = age > 18 ? "Allowed" : "Denied";
        assertEquals(expected, BaseJavaLesson.checkAccess(age));
    }

    static int[] randomScores() {
        int[] scores = new int[10];
        for (int i = 0; i < scores.length; i++) {
            scores[i] = RANDOM.nextInt(0, 101);
        }
        return scores;
    }

    @ParameterizedTest
    @MethodSource("randomScores")
    void getGradeTest(int score) {
        assertNotEquals("Error", BaseJavaLesson.getGrade(score));
    }
}
