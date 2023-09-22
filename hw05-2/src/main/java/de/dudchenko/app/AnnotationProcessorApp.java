package de.dudchenko.app;

import de.dudchenko.ProcessorTest;
import de.dudchenko.customannotations.CustomToString;

import java.lang.annotation.Annotation;

public class AnnotationProcessorApp {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        ProcessorTest processorTest = new ProcessorTest();


        System.out.println(processorTest.getA());
        System.out.println(processorTest.toString());

    }
}