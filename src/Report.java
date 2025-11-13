package src;

import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Report {
    final private String reportID, date;

    public Report(String reportID, String date){
        this.reportID = reportID;
        this.date = date;
    }

    public String getReportID() {
        return reportID;
    }

    public String getDate() {
        return date;
    }

    public String toString() {
        String detail =  "\nReport ID: " + getReportID() + "    Date: " + getDate();
        return detail;
    }

    public abstract void displayReport(Scanner scanner);

    public void showMenu(){
        Scanner scanner = new Scanner(System.in);
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Report report = null;
        int option = 0;

        while(option != 4){
            System.out.println("\n--- Bakery Report Menu ---");
            System.out.println("1. Check Inventory Report");
            System.out.println("2. Check Sales Report");
            System.out.println("3. Check Sales History");
            System.out.println("4. Return");
            System.out.print("Enter your options (1-4): ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    report = new InventoryReport("R001", formattedDate);
                    report.displayReport(scanner);
                    System.out.print("\n");
                    break;
                case 2:
                    report = new SalesReport("R002", formattedDate);
                    report.displayReport(scanner);
                    System.out.print("\n");
                    break;
                case 3:
                    report = new SalesHistoryReport("R003", formattedDate);
                    report.displayReport(scanner);
                    System.out.print("\n");
                    break;
                case 4:
                    System.out.println("\nReturning menu......");
                    break;
                default:
                    System.out.println("\nInvalid option! Please try again!");
                    break;
            }
        }
    }

//==================================================================================================================================================================//

    public static class InventoryReport extends Report{
        public InventoryReport(String reportID, String date){
            super(reportID, date);
        }

        public void displayReport(Scanner scanner){
            System.out.println(this);

            String csvFile = "src/Inventory.csv";
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
    }

//==================================================================================================================================================================//

    public static class SalesReport extends Report{
        public SalesReport(String reportID, String date){
            super(reportID, date);
        }

        public void displayReport(Scanner scanner){

            System.out.println(this);

            int selection = 0;

            while(selection != 6){
                System.out.println("\n1. Monthly Sales Report");
                System.out.println("2. Yearly Sales Report");
                System.out.println("3. Check Highest and Lowest Selling Month");
                System.out.println("4. Check Highest and Lowest Selling Product");
                System.out.println("5. Total Sales");
                System.out.println("6. Return to menu");
                System.out.print("Enter your options (1-6): ");

                selection = scanner.nextInt();
                scanner.nextLine();

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
                        sellingMonthHL(scanner);
                        System.out.print("\n");
                        break;
                    case 4:
                        sellingProductHL();
                        System.out.print("\n");
                        break;
                    case 5:
                        totalSales();
                        System.out.print("\n");
                        break;
                    case 6:
                        System.out.println("\nReturning to menu......");
                        break;
                    default:
                        System.out.println("\nInvalid option! Please try again!");
                        break;
                }
            }
        }

        private void monthlySR(){
            String csvFile = "src/Sales.csv";
            String line;

            double[] monthTotals = new double[12];

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length < 8) continue;

                    String date = data[7]; // format: dd-MM-yyyy
                    int monthIndex = Integer.parseInt(date.substring(3, 5)) - 1;
                    double total = Double.parseDouble(data[6]);

                    monthTotals[monthIndex] += total;
                }

                System.out.println("\n---- Monthly Sales Report ----\n");
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

        private void yearlySR(){
            String csvFile = "src/Sales.csv";
            String line;

            // Max 100 different years
            String[] years = new String[100];
            double[] totals = new double[100];
            int yearCount = 0; // keeps track of how many unique years

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                br.readLine();

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

        private void sellingMonthHL(Scanner scanner) {
            System.out.print("\nEnter the year to check (e.g. 2025): ");
            String inputYear = scanner.nextLine();

            String csvFile = "src/Sales.csv";
            String line;
            double[] monthTotals = new double[12];

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length < 8) continue;

                    String date = data[7];
                    String year = date.substring(6);

                    if (!year.equals(inputYear)) continue;

                    int monthIndex = Integer.parseInt(date.substring(3,5)) - 1;
                    double total = Double.parseDouble(data[6]);
                    monthTotals[monthIndex] += total;
                }

                String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

                // Find highest and lowest
                int highestIndex = -1, lowestIndex = -1;
                double highest = -1, lowest = Double.MAX_VALUE;
                boolean hasData = false;

                for (int i = 0; i < 12; i++) {
                    if (monthTotals[i] > 0) hasData = true;

                    if (monthTotals[i] > highest) {
                        highest = monthTotals[i];
                        highestIndex = i;
                    }
                    if (monthTotals[i] < lowest && monthTotals[i] > 0) {
                        lowest = monthTotals[i];
                        lowestIndex = i;
                    }
                }

                if (!hasData) {
                    System.out.println("\nNo sales data found for the year " + inputYear);
                } else {
                    System.out.printf("\nYear %s Sales Summary:\n", inputYear);
                    System.out.printf("Highest Selling Month: %s with RM %.2f\n", monthNames[highestIndex], highest);
                    System.out.printf("Lowest Selling Month: %s with RM %.2f\n", monthNames[lowestIndex], lowest);
                }

            } catch (IOException e) {
                System.out.println("Error reading Sales.csv!");
                e.printStackTrace();
            }
        }

        private void sellingProductHL() {
            String csvFile = "src/Sales.csv";
            String line;

            String[] productNames = new String[100]; // store up to 100 different products
            int[] totalQuantities = new int[100];
            int productCount = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length < 5) continue;

                    String[] products = data[3].split(";");
                    String[] quantities = data[4].split(";");

                    for (int i = 0; i < products.length; i++) {
                        String product = products[i].trim();
                        int qty = Integer.parseInt(quantities[i].trim());

                        // check if product already exists
                        boolean found = false;
                        for (int j = 0; j < productCount; j++) {
                            if (productNames[j].equalsIgnoreCase(product)) {
                                totalQuantities[j] += qty;
                                found = true;
                                break;
                            }
                        }

                        // if new product
                        if (!found) {
                            productNames[productCount] = product;
                            totalQuantities[productCount] = qty;
                            productCount++;
                        }
                    }
                }

                if (productCount == 0) {
                    System.out.println("No product data found.");
                    return;
                }

                // find highest and lowest
                int highestIndex = 0;
                int lowestIndex = 0;

                for (int i = 1; i < productCount; i++) {
                    if (totalQuantities[i] > totalQuantities[highestIndex]) {
                        highestIndex = i;
                    }
                    if (totalQuantities[i] < totalQuantities[lowestIndex]) {
                        lowestIndex = i;
                    }
                }

                // display result
                System.out.println("\n---- Product Sales Summary ----");
                System.out.println("Highest Selling Product: " + productNames[highestIndex] + " (" + totalQuantities[highestIndex] + " units)");
                System.out.println("Lowest Selling Product: " + productNames[lowestIndex] + " (" + totalQuantities[lowestIndex] + " units)");

            } catch (IOException e) {
                System.out.println("Error reading Sales.csv!");
                e.printStackTrace();
            }
        }


        public void totalSales() {
            String csvFile = "src/Sales.csv";
            String line;
            double totalSales = 0;
            int totalTransactions = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length < 7) continue;

                    totalSales += Double.parseDouble(data[6]); // total price per transaction
                    totalTransactions++;
                }

                System.out.println("\n===== Company Sales Performance Summary =====");
                System.out.printf("Total Transactions: %d%n", totalTransactions);
                System.out.printf("Overall Total Sales: RM %.2f%n", totalSales);

                // Average per transaction
                double average = (totalTransactions > 0) ? totalSales / totalTransactions : 0;
                System.out.printf("Average Sales per Transaction: RM %.2f%n", average);

                // Performance remark
                if (totalSales >= 50000) {
                    System.out.println("Performance: Excellent! Keep up the great work!");
                } else if (totalSales >= 20000) {
                    System.out.println("Performance: Good. Sales are steady.");
                } else if (totalSales > 0) {
                    System.out.println("Performance: Below target. Consider reviewing marketing strategy.");
                } else {
                    System.out.println("No sales records found.");
                }

            } catch (IOException e) {
                System.out.println("Error reading Sales.csv for total sales!");
                e.printStackTrace();
            }
        }

    }

//==================================================================================================================================================================//

    public static class SalesHistoryReport extends Report{
        public SalesHistoryReport(String reportID, String date){
            super(reportID, date);
        }

        public void displayReport(Scanner scanner){

            System.out.println(this);

            int choice = 0;

            while(choice != 5){
                System.out.println(" ");
                System.out.println("1. Display all Records");
                System.out.println("2. Based on Report ID");
                System.out.println("3. Based on Customer ID");
                System.out.println("4. Based on Date");
                System.out.println("5. Return to menu");
                System.out.print("Enter your options (1-5): ");

                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        salesRecord();
                        System.out.print("\n");
                        break;
                    case 2:
                        checkWithRID(scanner);
                        System.out.print("\n");
                        break;
                    case 3:
                        checkWithCID(scanner);
                        System.out.print("\n");
                        break;
                    case 4:
                        checkWithDate(scanner);
                        System.out.print("\n");
                        break;
                    case 5:
                        System.out.println("\nReturning to menu......");
                        break;
                    default:
                        System.out.println("\nInvalid option! Please try again!");
                        break;
                }
            }
        }

        private void salesRecord(){
            String csvFile = "src/Sales.csv";
            String line;

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                System.out.println("\n----Sales Report----");
                System.out.printf("%-10s %-15s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                        "Report ID", "Customer ID", "Customer Name", "Product(s)", "Quantity", "Price(RM)", "Total", "Date");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");

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
                            System.out.printf("%-10s %-15s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                                    reportID,
                                    customerID,
                                    customerName,
                                    productList[i].trim(),
                                    quantityList[i].trim(),
                                    priceList[i].trim(),
                                    totals.trim(),
                                    date.trim());
                        }else if (i == 0) {
                            System.out.printf("%-10s %-15s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                                    reportID,
                                    customerID,
                                    customerName,
                                    productList[i].trim(),
                                    quantityList[i].trim(),
                                    priceList[i].trim(),
                                    "",
                                    "");
                        } else if (i < productList.length - 1) {
                            System.out.printf("%-10s %-15s %-15s %-30s %-15s %-15s %-15s %-15s%n",
                                    "",
                                    "",
                                    "",
                                    productList[i].trim(),
                                    quantityList[i].trim(),
                                    priceList[i].trim(),
                                    "",
                                    "");
                        } else {
                            System.out.printf("%-10s %-15s %-15s %-30s %-15s %-15s %-15s %-15s%n",
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

        private void checkWithRID(Scanner scanner){
            System.out.print("\nEnter Report ID to search (e.g. R001): ");
            String inputRID = scanner.nextLine();

            String csvFile = "src/Sales.csv";
            String line;
            boolean found = false;

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[0].equalsIgnoreCase(inputRID)) {
                        displaySHR(data);
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

        private void checkWithCID(Scanner scanner) {
            System.out.print("\nEnter Customer ID to search (e.g. C001): ");
            String inputCID = scanner.nextLine();

            String csvFile = "src/Sales.csv";
            String line;
            boolean found = false;

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[1].equalsIgnoreCase(inputCID)) {
                        displaySHR(data);
                        found = true;
                    }
                }

                if (!found) {
                    System.out.println("\nNo record found with Customer ID: " + inputCID);
                }

            } catch (IOException e) {
                System.out.println("Error reading sales history with Customer ID!");
                e.printStackTrace();
            }
        }

        private void checkWithDate(Scanner scanner){
            System.out.print("\nEnter Date to search (e.g. 01-01-2025): ");
            String inputDate = scanner.nextLine();

            String csvFile = "src/Sales.csv";
            String line;
            boolean found = false;

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[7].equalsIgnoreCase(inputDate)) {
                        displaySHR(data);
                        found = true;
                    }
                }

                if (!found) {
                    System.out.println("\nNo record found for Date: " + inputDate);
                }

            } catch (IOException e) {
                System.out.println("Error reading sales history with Date!");
                e.printStackTrace();
            }
        }

        private void displaySHR(String[] data){
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
                    // Shows all info
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
    }

    public static void main(String[] args) {
        // Get current date in dd/MM/yyyy format
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        Report menu = new Report("R000", today) {
            public void displayReport(Scanner scanner) {}
        };
        menu.showMenu();
    }


}
