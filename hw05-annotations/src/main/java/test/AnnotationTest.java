package test;

import de.dudchenko.annotations.AfterEach;
import de.dudchenko.annotations.BeforeEach;
import de.dudchenko.annotations.Test;

public class AnnotationTest {

    @BeforeEach
    void setUp() {
        System.out.println("I'm setUp method");
    }

    @AfterEach
    void tearDown() {
        System.out.println("I'm tearDown method");
    }

    @Test
    void firstTest() {
        System.out.println("I'm test N1");
    }

    @Test
    void secondTest() {
        System.out.println("I'm test N2");
    }


}
