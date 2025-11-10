package BakeryManagementSystem;

import java.util.*;
import java.io.*;

public class ManageReport {
    private int ID;
    private String Date;

    public ManageReport(int ID, String Date){
        this.ID = ID;
        this.Date = Date;
    }

    public static void displayReport(){
        Scanner scanner = new Scanner(System.in);

        int option = 0;

        while(option != 4){
            System.out.println("\n--- Bakery Report Menu ---");
            System.out.println("1. Check Inventory Report");
            System.out.println("2. Check Sales Report");
            System.out.println("3. Check Sales History");
            System.out.println("4. Exit");
            System.out.print("Enter your options (1-4): ");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    inventoryReport();
                    System.out.print("\n");
                    break;
                case 2:
                    salesReport();
                    System.out.print("\n");
                    break;
                case 3:
                    salesHistory();
                    System.out.print("\n");
                    break;
                case 4:
                    System.out.print("\n");
                    System.out.println("Exiting report menu......");
                    break;
                default:
                    System.out.print("\n");
                    System.out.println("Invalid option! Please try again!");
                    break;
            }
        }
    }

    public static void inventoryReport(){
        String csvFile = "BakeryManagementSystem/Inventory.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            System.out.println("\n----Inventory Report----");
            System.out.printf("%-10s %-30s %-15s %-10s%n",
                    "Item ID", "Bakery Item Name", "Price (RM)", "Quantity");
            System.out.println("--------------------------------------------------------------------");

            while ((line = br.readLine()) != null) {
                // Split by comma
                String[] values = line.split(",");

                if (values.length < 4) continue; // Skip incomplete rows

                // Skip header if it's already in file
                if (values[0].equalsIgnoreCase("Item ID")) continue;

                System.out.printf("%-10s %-30s %-15s %-10s%n",
                        values[0], values[1], values[2], values[3]);
            }
        } catch (IOException e) {
            System.out.println("Error reading Inventory.csv!");
            e.printStackTrace();
        }
    }

    public static void salesReport(){
        Scanner scanner = new Scanner(System.in);

        int selection = 0;

        while(selection != 3){
            System.out.println(" ");
            System.out.println("1. Monthly Sales Report");
            System.out.println("2. Yearly Sales Report");
            System.out.println("3. Exit to menu");
            System.out.print("Enter your options (1-3): ");

            selection = scanner.nextInt();

            switch (selection) {
                case 1:
                    monthlySR();
                    System.out.print("\n");
                    break;
                case 2:
                    yearlySR();
                    System.out.print("\n");
                    break;
                case 3:
                    System.out.print("\n");
                    System.out.println("Exiting to menu......");
                    break;
                default:
                    System.out.print("\n");
                    System.out.println("Invalid option! Please try again!");
                    break;
            }
        }
    }

    public static void monthlySR() {
        String csvFile = "BakeryManagementSystem/Sales.csv";
        String line;

        double[] monthTotals = new double[12]; // Jan=0, Feb=1, ..., Dec=11

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 8) continue;

                String date = data[7]; // format: dd-MM-yyyy
                int monthIndex = Integer.parseInt(date.substring(3, 5)) - 1;
                double total = Double.parseDouble(data[6]);

                monthTotals[monthIndex] += total;
            }

            System.out.println("\n---- Monthly Sales Report ----");
            System.out.println("Month      Total Sales (RM)");
            System.out.println("----------------------------");

            String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

            for (int i = 0; i < 12; i++) {
                if (monthTotals[i] > 0) {
                    System.out.printf("%-10s %.2f%n", monthNames[i], monthTotals[i]);
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading Sales.csv!");
            e.printStackTrace();
        }
    }

    public static void yearlySR() {
        String csvFile = "BakeryManagementSystem/Sales.csv";
        String line;

        // Max 100 different years (you can increase if needed)
        String[] years = new String[100];
        double[] totals = new double[100];
        int yearCount = 0; // keeps track of how many unique years

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 8) continue;

                String date = data[7]; // format dd-MM-yyyy
                String year = date.substring(6);
                double total = Double.parseDouble(data[6]);

                // Check if year already exists
                boolean found = false;
                for (int i = 0; i < yearCount; i++) {
                    if (years[i].equals(year)) {
                        totals[i] += total;
                        found = true;
                        break;
                    }
                }

                // If year not found, add it
                if (!found) {
                    years[yearCount] = year;
                    totals[yearCount] = total;
                    yearCount++;
                }
            }

            // Print report
            System.out.println("\n---- Yearly Sales Report ----");
            System.out.println("Year       Total Sales (RM)");
            System.out.println("----------------------------");

            for (int i = 0; i < yearCount; i++) {
                System.out.printf("%-10s %.2f%n", years[i], totals[i]);
            }

        } catch (Exception e) {
            System.out.println("Error reading Sales.csv!");
            e.printStackTrace();
        }
    }

    public static void salesHistory(){
        Scanner scanner = new Scanner(System.in);

        int choice = 0;

        while(choice != 5){
            System.out.println(" ");
            System.out.println("1. Display all Records");
            System.out.println("2. Based on Report ID");
            System.out.println("3. Based on Customer ID");
            System.out.println("4. Based on Date");
            System.out.println("5. Exit to menu");
            System.out.print("Enter your options (1-5): ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    salesRecord();
                    System.out.print("\n");
                    break;
                case 2:
                    checkWithRID();
                    System.out.print("\n");
                    break;
                case 3:
                    checkWithCID();
                    System.out.print("\n");
                    break;
                case 4:
                    checkWithDate();
                    System.out.print("\n");
                    break;
                case 5:
                    System.out.print("\n");
                    System.out.println("Exiting to menu......");
                    break;
                default:
                    System.out.print("\n");
                    System.out.println("Invalid option! Please try again!");
                    break;
            }
        }
    }

    public static void salesRecord(){
        String csvFile = "BakeryManagementSystem/Sales.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            System.out.println("\n----Sales Report----");
            System.out.printf("%-10s %-15s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                    "Report ID", "Customer ID", "Customer Name", "Product(s)", "Quantity", "Price(RM)", "Total", "Date");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

            while ((line = br.readLine()) != null) {
                // Split by comma
                String[] values = line.split(",");

                // Skip header if it's already in file
                if (values[0].equalsIgnoreCase("Report ID")) continue;

                // Get base columns
                String reportID = values[0];
                String customerID = values[1];
                String customerName = values[2];
                String products = values[3];
                String quantities = values[4];
                String prices = values[5];
                String totals = values[6];
                String date = values[7];

                // Split each field by semicolon if multiple products exist
                String[] productList = products.split(";");
                String[] quantityList = quantities.split(";");
                String[] priceList = prices.split(";");

                for (int i = 0; i < productList.length; i++) {
                    if (productList.length == 1) {
                        System.out.printf("%-10s %-10s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                                reportID,
                                customerID,
                                customerName,
                                productList[i].trim(),
                                quantityList[i].trim(),
                                priceList[i].trim(),
                                totals.trim(),
                                date.trim());
                    }else if (i == 0) {
                        System.out.printf("%-10s %-10s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                                reportID,
                                customerID,
                                customerName,
                                productList[i].trim(),
                                quantityList[i].trim(),
                                priceList[i].trim(),
                                "",
                                "");
                    } else if (i < productList.length - 1) {
                        System.out.printf("%-10s %-10s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                                "",
                                "",
                                "",
                                productList[i].trim(),
                                quantityList[i].trim(),
                                priceList[i].trim(),
                                "",
                                "");
                    } else {
                        System.out.printf("%-10s %-10s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                                "",
                                "",
                                "",
                                productList[i].trim(),
                                quantityList[i].trim(),
                                priceList[i].trim(),
                                totals.trim(),
                                date.trim());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Sales.csv!");
            e.printStackTrace();
        }
    }

    public static void checkWithRID() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n");
        System.out.print("Enter Report ID to search (e.g. R001): ");
        String inputRID = scanner.nextLine();

        String csvFile = "BakeryManagementSystem/Sales.csv";
        String line;
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equalsIgnoreCase(inputRID)) {
                    displaySalesRecord(data);
                    found = true;
                }
            }

            if (!found) {
                System.out.print("\n");
                System.out.println("No record found with Report ID: " + inputRID);
            }

        } catch (IOException e) {
            System.out.println("Error reading sales history with Report ID!");
            e.printStackTrace();
        }
    }

    public static void checkWithCID() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n");
        System.out.print("Enter Customer ID to search (e.g. C001): ");
        String inputCID = scanner.nextLine();

        String csvFile = "BakeryManagementSystem/Sales.csv";
        String line;
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equalsIgnoreCase(inputCID)) {
                    displaySalesRecord(data);
                    found = true;
                }
            }

            if (!found) {
                System.out.print("\n");
                System.out.println("No record found with Customer ID: " + inputCID);
            }

        } catch (IOException e) {
            System.out.println("Error reading sales history with Customer ID!");
            e.printStackTrace();
        }
    }

    public static void checkWithDate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n");
        System.out.print("Enter Date to search (e.g. 01-01-2025): ");
        String inputDate = scanner.nextLine();

        String csvFile = "BakeryManagementSystem/Sales.csv";
        String line;
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[7].equalsIgnoreCase(inputDate)) {
                    displaySalesRecord(data);
                    found = true;
                }
            }

            if (!found) {
                System.out.print("\n");
                System.out.println("No record found for Date: " + inputDate);
            }

        } catch (IOException e) {
            System.out.println("Error reading sales history with Date!");
            e.printStackTrace();
        }
    }

    private static void displaySalesRecord(String[] data) {
        String reportID = data[0].trim();
        String custID = data[1].trim();
        String custName = data[2].trim();
        String[] products = data[3].split(";");
        String[] quantities = data[4].split(";");
        String[] prices = data[5].split(";");
        String total = data[6].trim();
        String date = data[7].trim();

        System.out.printf("%-10s %-15s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                "ReportID", "Customer ID", "Customer Name", "Product", "Quantity", "Price", "Total", "Date");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < products.length; i++) {
            if (i == 0) {
                // First line shows all info
                System.out.printf("%-10s %-15s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                        reportID, custID, custName, products[i],
                        quantities[i], prices[i], (i == products.length - 1 ? total : ""), (i == products.length - 1 ? date : ""));
            } else {
                // Subsequent lines are indented for products
                System.out.printf("%-10s %-15s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                        "", "", "", products[i],
                        quantities[i], prices[i], (i == products.length - 1 ? total : ""), (i == products.length - 1 ? date : ""));
            }
        }
    }

    public static void main(String[] args) {
        ManageReport.displayReport();
    }
}
