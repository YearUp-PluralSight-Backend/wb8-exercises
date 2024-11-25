package com.pluralsight;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger log = LogManager.getLogger(); // reflection to get the class name

    public static void main(String[] args) {
        log.info("Hello, World!");
    }

    public static void logMeLikeYouDo() {
        log.info("I'm logging like you do");
    }
}