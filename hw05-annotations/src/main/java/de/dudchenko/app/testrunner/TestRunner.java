package de.dudchenko.app.testrunner;

import de.dudchenko.annotations.AfterEach;
import de.dudchenko.annotations.BeforeEach;
import de.dudchenko.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private Method beforMethod = null;
    private Method afterMethod = null;
    private final List<Method> testMethods = new ArrayList<>();

//    private TestRunner testRunner = new TestRunner();

    public void run(Class<?> clazzToTest) throws Exception {
        sortMethodsOf(clazzToTest);
        runTestsOf(clazzToTest);
    }

    private void sortMethodsOf(Class<?> clazzToTest) {
        resetMethods();

        for (Method method : clazzToTest.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeEach.class)) {
                beforMethod = method;
            } else if (method.isAnnotationPresent(AfterEach.class)) {
                afterMethod = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }
    }

    private void runTestsOf(Class<?> clazzToTest) throws Exception {
        for (Method testMethod : testMethods) {
            Object testInstance = clazzToTest.getDeclaredConstructor().newInstance();

            try {
                callMethodOn(beforMethod, testInstance);
                callMethodOn(testMethod, testInstance);
                callMethodOn(afterMethod, testInstance);
                System.out.println(testMethod.getName() + " passed");
            } catch (Exception ex) {
                System.out.println(testMethod.getName() + " failed");
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
