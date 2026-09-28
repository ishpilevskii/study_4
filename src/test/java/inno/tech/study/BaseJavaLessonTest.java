package inno.tech.study;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    private static void printResult(boolean passed) {
        if (passed) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @Test
    void isEvenTest() {
        int n = RANDOM.nextInt(1, 101);
        boolean expected = n % 2 == 0;
        boolean actual = BaseJavaLesson.isEven(n);
        printResult(actual == expected);
    }

    @Test
    void isPositiveTest() {
        int n = RANDOM.nextInt(-100, 101);
        boolean expected = n >= 0;
        boolean actual = BaseJavaLesson.isPositive(n);
        printResult(actual == expected);
    }

    @RepeatedTest(20)
    void checkAccessTest() {
        int age = RANDOM.nextInt(0, 100);
        String expected = age > 18 ? "Allowed" : "Denied";
        String actual = BaseJavaLesson.checkAccess(age);
        printResult(actual.equals(expected));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 10, 50, 100})
    void sumToNTest(int n) {
        int expected = n * (n + 1) / 2;
        int actual = BaseJavaLesson.sumToN(n);
        printResult(actual == expected);
    }

    static List<Arguments> bugMessages() {
        return List.of(
                Arguments.of(new String[]{"Info", "Bug", "Error"}, true),
                Arguments.of(new String[]{"Info", "Warning", "Error"}, false)
        );
    }

    @ParameterizedTest
    @MethodSource("bugMessages")
    void hasBugTest(String[] messages, boolean expected) {
        boolean actual = BaseJavaLesson.hasBug(messages);
        printResult(actual == expected);
    }

    static List<Arguments> reverseArrays() {
        return List.of(
                Arguments.of(new String[]{"a", "b", "c"}, new String[]{"c", "b", "a"}),
                Arguments.of(new String[]{"one"}, new String[]{"one"}),
                Arguments.of(new String[]{"1", "2", "3", "4"}, new String[]{"4", "3", "2", "1"})
        );
    }

    @ParameterizedTest
    @MethodSource("reverseArrays")
    void reverseTest(String[] input, String[] expected) {
        String[] actual = BaseJavaLesson.reverse(input);
        printResult(Arrays.equals(actual, expected));
    }

    @ParameterizedTest
    @CsvSource(quoteCharacter = '"', value = {
            "0, Let's go!",
            "1, 1 Let's go!",
            "3, 3 2 1 Let's go!",
            "5, 5 4 3 2 1 Let's go!"
    })
    void blastOffTest(int start, String expected) {
        String actual = BaseJavaLesson.blastOff(start);
        printResult(actual.equals(expected));
    }

    @ParameterizedTest
    @CsvSource({
            "5, E",
            "30, D",
            "50, C",
            "70, B",
            "90, A",
            "-1, Error",
            "101, Error"
    })
    void getGradeTest(int score, String expected) {
        String actual = BaseJavaLesson.getGrade(score);
        printResult(actual.equals(expected));
    }

    enum MaxCase {
        POSITIVE(new int[]{1, 5, 3}, 5),
        NEGATIVE(new int[]{-7, -2, -9}, -2),
        MIXED(new int[]{-10, 0, 10}, 10),
        SINGLE(new int[]{42}, 42);

        private final int[] arr;
        private final int expected;

        MaxCase(int[] arr, int expected) {
            this.arr = arr;
            this.expected = expected;
        }
    }

    @ParameterizedTest
    @EnumSource(MaxCase.class)
    void findMaxTest(MaxCase testCase) {
        int actual = BaseJavaLesson.findMax(testCase.arr);
        printResult(actual == testCase.expected);
    }

    static List<Arguments> averageLists() {
        return List.of(
                Arguments.of(List.of(1, 2, 3), 2),
                Arguments.of(List.of(10, 20, 30, 40), 25),
                Arguments.of(List.of(5), 5),
                Arguments.of(List.of(1, 2), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("averageLists")
    void calcAverageTest(List<Integer> list, int expected) {
        int actual = BaseJavaLesson.calcAverage(list);
        printResult(actual == expected);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/get_even_in_range.csv", numLinesToSkip = 1)
    void getEvenInRangeTest(int start, int end, String expected) {
        String actual = BaseJavaLesson.getEvenInRange(start, end);
        printResult(actual.equals(expected));
    }

    static List<Arguments> namesToRemove() {
        return List.of(
                Arguments.of(List.of("Anna", "Ivan", "Anna", "Oleg"), "Anna", List.of("Ivan", "Oleg")),
                Arguments.of(List.of("Ivan", "Petr", "Olga"), "Petr", List.of("Ivan", "Olga")),
                Arguments.of(List.of("Masha", "Dima"), "Oleg", List.of("Masha", "Dima")),
                Arguments.of(List.of("Oleg", "Oleg", "Ivan"), "Oleg", List.of("Ivan"))
        );
    }

    @ParameterizedTest
    @MethodSource("namesToRemove")
    void removeSpecificNameTest(List<String> list, String nameToRemove, List<String> expected) {
        List<String> actual = BaseJavaLesson.removeSpecificName(list, nameToRemove);
        printResult(actual.equals(expected));
    }
}
