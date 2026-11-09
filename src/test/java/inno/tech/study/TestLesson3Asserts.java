package inno.tech.study;

import org.junit.jupiter.api.Test;

import java.util.List;

import static inno.tech.study.BaseJavaLesson.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TestLesson3Asserts {

    @Test
    void expectedTrueGetFalse() {
        assertThat(isEven(3)).as("Number is not even").isTrue();
    }

    @Test
    void expectedFalseGetFalse() {
        assertThat(isEven(5)).as("Number is not even").isTrue();
    }

    @Test
    void checkValueInList() {
        String expectedName = "Roma";
        List<String> namesActual = removeSpecificName(List.of("Masha", "Roma", "Vasya", "Dima"), "Masha");
        assertThat(namesActual).as("List not contains " + expectedName).contains(expectedName);
    }

    @Test
    void checkStringEqual() {
        String expectedGrade = "A";
        assertThat(getGrade(50)).as("Grade not equal " + expectedGrade).isEqualTo(expectedGrade);
    }



}
