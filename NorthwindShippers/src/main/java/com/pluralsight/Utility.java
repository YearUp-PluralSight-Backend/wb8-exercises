package com.pluralsight;

import java.util.Scanner;

public class Utility {

    private static final Scanner SCANNER = new Scanner(System.in);


    public static String getStringInput(String prompt) {
        String input = null;
        try {
            input = null;
            boolean isValidInput = false;
            while (!isValidInput) {
                System.out.print(prompt);
                input = SCANNER.nextLine().trim();
                if (input != null && !input.trim().isEmpty()) {
                    isValidInput = true;
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    public static int getIntInput(String prompt) {
        int input = 0;
        try {
            input = 0;
            boolean isValidInput = false;
            while (!isValidInput) {
                System.out.print(prompt);
                try {
                    input = Integer.parseInt(SCANNER.nextLine().trim());
                    isValidInput = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please try again.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return input;
    }
}
