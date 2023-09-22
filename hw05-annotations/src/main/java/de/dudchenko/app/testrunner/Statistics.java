package de.dudchenko.app.testrunner;

public class Statistics {
    // ANSI escape codes for some basic colors
    public static final String GREEN_COLOR = "\u001B[32m";
    public static final String RED_COLOR = "\u001B[31m";
    public static final String RESET_COLOR = "\u001B[0m";

    private int passed;
    private int failed;
    private int totalFound;
    private int totalExecuted;

    public void addPassed(String methodName) {
        passed++;
        totalExecuted++;
        System.out.println(GREEN_COLOR + methodName + " passed" + RESET_COLOR);
    }

    public void addFailed(String methodName) {
        failed++;
        totalExecuted++;
        System.out.println(RED_COLOR + methodName + " failed" + RESET_COLOR);
    }

    public void addFound() {
        totalFound++;
    }

    public void printSummery() {
        System.out.println();
        System.out.println("Total tests found: " + totalFound);
        System.out.println("Total tests executed: " + totalExecuted);
        System.out.println(GREEN_COLOR + "Total tests passed: " + passed + RESET_COLOR);
        System.out.println(RED_COLOR + "Total tests failed: " + failed + RESET_COLOR);
    }
}