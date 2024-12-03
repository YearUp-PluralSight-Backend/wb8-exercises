package com.pluralsight;

public class Main {
    public static void main(String[] args) {

        ShipperDataManager dataManager = new ShipperDataManager("northwind");
//        1.
        dataManager.insertShipper(Utility.getStringInput("Company Name: "), Utility.getStringInput("Phone: "));
//        2.
        dataManager.displayShippers();
//        3.
        dataManager.updateShipper(Utility.getIntInput("Shipper ID: "), Utility.getStringInput("Phone: "));
//        4.
        dataManager.displayShippers();
//        5.
        dataManager.deleteShipper(Utility.getIntInput("Shipper ID: ")); // 4
//        6.
        dataManager.displayShippers();

    }
}