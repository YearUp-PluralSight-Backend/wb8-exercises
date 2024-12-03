package com.pluralsight.utils;

import java.util.Locale;
import java.util.Scanner;

public class Utility {

    private static final Scanner SCANNER = new Scanner(System.in);


    public static String getInput(String prompt) {
        try {
            String value = "";
            while (value.isEmpty()) {
                System.out.print(prompt);
                value = SCANNER.nextLine().trim().toUpperCase(Locale.ROOT);
                if (value.isEmpty()) {
                    System.out.println("Please enter a valid value.");
                }
            }
            return value;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }
}
