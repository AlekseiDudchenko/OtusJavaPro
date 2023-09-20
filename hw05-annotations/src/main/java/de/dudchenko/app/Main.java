package de.dudchenko.app;

import de.dudchenko.app.testrunner.TestRunner;
import test.AnnotationTest;

public class Main {
    public static void main(String[] args) throws Exception {
        TestRunner testRunner = new TestRunner();

        testRunner.run(AnnotationTest.class);
    }
}
