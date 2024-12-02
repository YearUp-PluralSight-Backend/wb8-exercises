package com.pluralsight.util;

import java.util.Scanner;

public class Utility {

    public static int getInt(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        try{
            int option = scanner.nextInt();
            System.out.println("You entered: " + option);
            return option;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }
}
