package de.dudchenko;

import com.google.common.base.Joiner;

public class HelloOtus {
    public static void main(String[] args) {
        String string = Joiner.on(", ").join("Hello", "World"); // returns "1,5,7"
        System.out.println(string);
    }
}