package de.dudchenko.annotations.app;

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

    public void run(Class<?> clazzToTest) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Object testInstance = clazzToTest.getDeclaredConstructor().newInstance();

        sortMethodsOf(testInstance);
        runTestsOf(testInstance);
    }

    private void sortMethodsOf(Object testInstance) {
        for (Method method : testInstance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeEach.class)) {
                beforMethod = method;
            } else if (method.isAnnotationPresent(AfterEach.class)) {
                afterMethod = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }
    }

    private void runTestsOf(Object testInstance) throws IllegalAccessException, InvocationTargetException {
        for (Method testMethod : testMethods) {
            callMethodOn(beforMethod, testInstance);
            callMethodOn(testMethod, testInstance);
            callMethodOn(afterMethod, testInstance);
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
}
