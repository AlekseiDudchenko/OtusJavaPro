package de.dudchenko.annotations.app;

import test.AnnotationTest;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        TestRunner testRunner = new TestRunner();

        testRunner.run(AnnotationTest.class);
    }
}
