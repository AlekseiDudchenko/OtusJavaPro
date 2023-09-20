package de.dudchenko.annotations.app;

import de.dudchenko.annotations.AfterEach;
import de.dudchenko.annotations.BeforeEach;
import de.dudchenko.annotations.Test;
import test.AnnotationTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = AnnotationTest.class;
        Object testInstance = clazz.getDeclaredConstructor().newInstance();

        Method beforMethod = null;
        Method afterMethod = null;
        List<Method> testMethods = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeEach.class)) {
                beforMethod = method;
            } else if (method.isAnnotationPresent(AfterEach.class)) {
                afterMethod = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }

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
