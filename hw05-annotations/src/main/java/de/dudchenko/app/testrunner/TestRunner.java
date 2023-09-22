package de.dudchenko.app.testrunner;

import de.dudchenko.annotations.AfterEach;
import de.dudchenko.annotations.BeforeEach;
import de.dudchenko.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private final Class<?> clazzToTest;
    private Method beforMethod = null;
    private Method afterMethod = null;
    private final List<Method> testMethods = new ArrayList<>();
    private final Statistics statistics;

    public TestRunner(Class<?> clazzToTest) {
        this.clazzToTest = clazzToTest;
        statistics = new Statistics();
    }

    public void run() throws Exception {
        sortMethods();
        runTests();
        statistics.printSummery();
    }

    private void sortMethods() {
        resetMethods();

        for (Method method : clazzToTest.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeEach.class)) {
                beforMethod = method;
            } else if (method.isAnnotationPresent(AfterEach.class)) {
                afterMethod = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
                statistics.addFound();
            }
        }
    }

    private void runTests() throws Exception {
        for (Method testMethod : testMethods) {
            Object testInstance = clazzToTest.getDeclaredConstructor().newInstance();

            try {
                callMethodOn(beforMethod, testInstance);
                callMethodOn(testMethod, testInstance);
                callMethodOn(afterMethod, testInstance);
                statistics.addPassed(testMethod.getName());
            } catch (Exception ex) {
                statistics.addFailed(testMethod.getName());
            }
        }
    }

    private static void callMethodOn(Method method, Object instance) throws IllegalAccessException, InvocationTargetException {
        if (method == null || instance == null) {
            return;
        }

        if (method.canAccess(instance)) {
            method.invoke(instance);
        } else {
            method.setAccessible(true);
            method.invoke(instance);
            method.setAccessible(false);
        }
    }

    private void resetMethods() {
        beforMethod = null;
        afterMethod = null;
        testMethods.clear();
    }
}
