import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Person {
    protected String name;
    protected String dateOfBirth;
    protected String id;
    protected String phoneNumber;

    static Scanner scanner = new Scanner(System.in);

    public Person(String name, String dateOfBirth, String phoneNumber) {
        this.dateOfBirth = dateOfBirth;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String[] getInfo() {
        return new String[]{id,name,dateOfBirth,phoneNumber};
    }

    // --- Shared ID generation logic ---
    protected static String generateNewId(String fileName, String prefix) {
        String lastId = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lastId = line.split(",")[0];
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing file found for " + prefix + ". Starting at " + prefix + "001");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lastId == null) {
            return prefix + "001";
        } else {
            int num = Integer.parseInt(lastId.substring(1));
            num++;
            return String.format("%s%03d", prefix, num);
        }
    }

    // --- Shared save logic ---
    protected static void saveToFile(String fileName, String record) {
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.write(record + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Shared read logic ---
    protected static void readFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("No records found in " + fileName);
        }
    }

    protected static void showUI(char type, String[] user){
        boolean exit = false;
        String choice = "";

        // Create a Customer or Manager instance from user data
        Customer customer = null;
        Manager manager = null;

        if (type == 'C') {
            customer = new Customer(user[1], user[2], user[3]);
            customer.id = user[0]; // Set the existing ID
        } else if (type == 'M') {
            manager = new Manager(user[1], user[2], user[3]);
            manager.id = user[0]; // Set the existing ID
        }

        while (!exit) {
            if (type == 'C') {
                System.out.println("\n=====================================");
                System.out.println("     🍞      Customer Menu");
                System.out.println("=====================================");
                System.out.println("1. Make Order");
                System.out.println("2. View Order Status");
                System.out.println("3. View Order History");
                System.out.println("4. Update Information");
                System.out.println("5. Log Out");

                choice = scanner.next();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case "1":
                        customer.makeOrder();
                        break;
                    case "2":
                        customer.viewOrderStatus();
                        break;
                    case "3":
                        Report.showCPHR(user[0]);
                        break;
                    case "4":
                        user = updateInfoUI(type, user);
                        customer.name = user[1];
                        customer.dateOfBirth = user[2];
                        customer.phoneNumber = user[3];
                        break;
                    case "5":
                        exit = true;
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }

            } else if (type == 'M') {
                System.out.println("\n=====================================");
                System.out.println("     🍞       Manager Menu");
                System.out.println("=====================================");
                System.out.println("1. Show Orders");
                System.out.println("2. Complete Order");
                System.out.println("3. Cancel Orders");
                System.out.println("4. View Customer List");
                System.out.println("5. View Manager List");
                System.out.println("6. View Sales Report");
                System.out.println("7. Update Product Stock");
                System.out.println("8. Add Product");
                System.out.println("9. Remove Product");
                System.out.println("10. Update Information");
                System.out.println("11. Log Out");

                choice = scanner.next();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case "1":
                        manager.showOrders();
                        break;
                    case "2":
                        manager.updateOrderStatus();
                        break;
                    case "3":
                        manager.cancelOrder();
                        break;
                    case "4":
                        Customer.readCustomers();
                        break;
                    case "5":
                        Manager.readManagers();
                        break;
                    case "6":
                        Report.showSRMenu();
                        break;
                    case "7":
                        ProductManagement.editProduct();
                        break;
                    case "8":
                        ProductManagement.addProduct();
                        break;
                    case "9":
                        ProductManagement.deleteProduct();
                        break;
                    case "10":
                        user = updateInfoUI(type, user);
                        manager.name = user[1];
                        manager.dateOfBirth = user[2];
                        manager.phoneNumber = user[3];
                        break;
                    case "11":
                        exit = true;
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        }
    }

    protected static String[] updateInfoUI(char type, String[] user){
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Contact Number");
        System.out.println("3. Return");
        String uChoice = scanner.next();
        scanner.nextLine(); // consume newline
        String change = "";

        switch (uChoice) {
            case "1":
                System.out.print("New Name: ");
                change = scanner.nextLine();
                user[1] = change;
                if(type == 'C'){
                    Customer.updateCustomerInfo(user);
                } else {
                    Manager.updateManagerInfo(user);
                }
                System.out.println("Update Successful!");
                break;
            case "2":
                System.out.print("New Contact Number: ");
                change = scanner.nextLine();
                user[3] = change;
                if(type == 'C'){
                    Customer.updateCustomerInfo(user);
                } else {
                    Manager.updateManagerInfo(user);
                }
                System.out.println("Update Successful!");
                break;
            case "3":
                System.out.println("Returning to menu...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

        return user;
    }
}




//====================================================================================================




class Customer extends Person {
    private static final String FILE = System.getProperty("user.dir") + "/BakeryManagementSystem/src/customers.csv";
    private static final String ORDERS_FILE =System.getProperty("user.dir") + "/BakeryManagementSystem/src/orders.csv";

    public Customer(String name, String dateOfBirth, String phoneNumber) {
        super(name, dateOfBirth, phoneNumber);
        this.id = generateNewId(FILE, "C");
    }

    protected void makeOrder(){
        String[] order = new String[0];
        ArrayList<String[]> cart = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        // Choose order type
        System.out.println("\n🍞 Select Order Type:");
        System.out.println("1. Take-Away");
        System.out.println("2. Dine-In");
        System.out.print("Enter choice: ");
        String orderTypeChoice = sc.next();
        String orderType = "";

        switch(orderTypeChoice) {
            case "1":
                orderType = "Take-Away";
                break;
            case "2":
                orderType = "Dine-In";
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Take-Away.");
                orderType = "Take-Away";
                break;
        }

        ProductManagement.viewProducts();
        System.out.print("Enter Product ID you would like to get: ");
        int id = sc.nextInt();
        System.out.print("Enter the amount you would like to get: ");
        int amount = sc.nextInt();
        order = ProductManagement.checkStock(id, amount);

        if(order.length == 0){
            System.out.println("Insufficient stock");
            return;
        } else {
            cart.add(order);
            System.out.println("Order added successfully");
        }

        boolean fin = false;
        char reply;

        while(!fin){
            System.out.print("Would you like to order another product? (Y/N): ");
            reply = sc.next().charAt(0);

            switch(reply){
                case 'Y':
                case 'y':
                    ProductManagement.viewProducts();
                    System.out.print("Enter Product ID you would like to get: ");
                    id = sc.nextInt();
                    System.out.print("Enter the amount you would like to get: ");
                    amount = sc.nextInt();
                    order = ProductManagement.checkStock(id, amount);

                    if(order.length == 0){
                        System.out.println("Insufficient stock");
                    } else {
                        cart.add(order);
                        System.out.println("Order added successfully");
                    }
                    break;

                case 'N':
                case 'n':
                    fin = true;
                    if(!cart.isEmpty()){
                        double total = checkcart(cart);
                        payment(cart, total, orderType);
                    }
                    break;

                default:
                    System.out.println("Please Enter a valid choice!");
                    break;
            }
        }
    }

    protected double checkcart(ArrayList<String[]> cart){
        double total = 0;
        System.out.println("\n-------------Cart--------------");
        System.out.println("\n========================================================================================");
        System.out.printf("| %-3s | %-12s | %-40s | %-10s | %-6s | %-11s |\n",
                "ID", "Category", "Name", "Price", "Amount", "Total Price");
        System.out.println("----------------------------------------------------------------------------------------");

        int num = 0;
        while(num < cart.size()){
            double itemTotal = Double.parseDouble(cart.get(num)[2]) * Integer.parseInt(cart.get(num)[4]);
            System.out.printf("| %-3s | %-12s | %-40s | %-10s | %-6s | %-11.2f |\n",
                    cart.get(num)[3], cart.get(num)[0], cart.get(num)[1],
                    cart.get(num)[2], cart.get(num)[4], itemTotal);
            total += itemTotal;
            num++;
        }
        System.out.println("========================================================================================");
        System.out.printf("Total Price: RM %.2f\n", total);
        return total;
    }

    protected void payment(ArrayList<String[]> cart, double total, String orderType){
        Scanner sc = new Scanner(System.in);

        // Payment method selection
        System.out.println("\n💳 Select Payment Method:");
        System.out.println("1. Cash");
        System.out.println("2. Credit/Debit Card");
        System.out.println("3. E-Wallet");
        System.out.print("Enter choice: ");
        String paymentChoice = sc.next();
        String paymentMethod = "";

        switch(paymentChoice) {
            case "1":
                paymentMethod = "Cash";
                System.out.print("Enter cash amount: RM ");
                double cashGiven = sc.nextDouble();
                if (cashGiven < total) {
                    System.out.println("Insufficient amount. Payment cancelled.");
                    return;
                }
                double change = cashGiven - total;
                System.out.printf("Change: RM %.2f\n", change);
                break;
            case "2":
                paymentMethod = "Card";
                System.out.println("Processing card payment...");
                break;
            case "3":
                paymentMethod = "E-Wallet";
                System.out.println("Processing e-wallet payment...");
                break;
            default:
                System.out.println("Invalid choice. Payment cancelled.");
                return;
        }

        // Generate order ID and save order
        String orderId = generateNewId(ORDERS_FILE, "O");

        // cart structure: [0]=category, [1]=name, [2]=price, [3]=productId, [4]=quantity
        StringBuilder product = new StringBuilder(cart.get(0)[1]);
        StringBuilder quantity = new StringBuilder(cart.get(0)[4]);
        StringBuilder price = new StringBuilder(cart.get(0)[2]);

        // Reduce stock using productId (index 3) and quantity (index 4)
        ProductManagement.reduceStock(Integer.parseInt(cart.get(0)[3]), Integer.parseInt(cart.get(0)[4]));

        int num = 1;
        while(num < cart.size()){
            product.append(";").append(cart.get(num)[1]);
            quantity.append(";").append(cart.get(num)[4]);
            price.append(";").append(cart.get(num)[2]);
            ProductManagement.reduceStock(Integer.parseInt(cart.get(num)[3]), Integer.parseInt(cart.get(num)[4]));
            num++;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // Save to orders.csv: OrderID,CustomerID,CustomerName,Products,Quantities,Prices,Total,OrderType,PaymentMethod,Status,DateTime
        saveToFile(ORDERS_FILE, orderId + "," + this.id + "," + name + "," +
                product + "," + quantity + "," + price + "," + total + "," +
                orderType + "," + paymentMethod + ",Pending," + formattedDateTime);

        // Also save to Sales.csv for backward compatibility
        String ScsvFile = System.getProperty("user.dir") + "/BakeryManagementSystem/src/Sales.csv";
        String salesId = generateNewId(ScsvFile, "R");
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(dateFormatter);

        saveToFile(ScsvFile, salesId + "," + this.id + "," + name + "," +
                product + "," + quantity + "," + price + "," + total + "," + formattedDate);

        // Generate receipt
        generateReceipt(orderId, cart, total, orderType, paymentMethod, formattedDateTime);

        System.out.println("\n✅ Payment successful!");
        System.out.println("Order ID: " + orderId);
        System.out.println("Order Status: Pending");
        System.out.println("\nThank you for your purchase!");
        System.out.println("Product stock has been updated.");
    }

    protected void generateReceipt(String orderId, ArrayList<String[]> cart, double total,
                                   String orderType, String paymentMethod, String dateTime) {
        String receiptFile = "receipt_" + orderId + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(receiptFile))) {
            writer.println("===============================================");
            writer.println("           🍞 BAKERY RECEIPT 🍞");
            writer.println("===============================================");
            writer.println("Order ID: " + orderId);
            writer.println("Customer ID: " + this.id);
            writer.println("Customer Name: " + this.name);
            writer.println("Date & Time: " + dateTime);
            writer.println("Order Type: " + orderType);
            writer.println("Payment Method: " + paymentMethod);
            writer.println("===============================================");
            writer.println();
            writer.printf("%-30s %5s %10s %10s\n", "Item", "Qty", "Price", "Total");
            writer.println("-----------------------------------------------");

            for (String[] item : cart) {
                String itemName = item[1];
                if (itemName.length() > 30) {
                    itemName = itemName.substring(0, 27) + "...";
                }
                int qty = Integer.parseInt(item[4]);
                double price = Double.parseDouble(item[2]);
                double itemTotal = price * qty;

                writer.printf("%-30s %5d %10.2f %10.2f\n", itemName, qty, price, itemTotal);
            }

            writer.println("-----------------------------------------------");
            writer.printf("%-30s %16s %.2f\n", "TOTAL", "RM", total);
            writer.println("===============================================");
            writer.println("        Thank you for your purchase!");
            writer.println("          Please come again! 😊");
            writer.println("===============================================");

            System.out.println("\n📄 Receipt generated: " + receiptFile);

        } catch (IOException e) {
            System.out.println("Error generating receipt: " + e.getMessage());
        }
    }

    protected void viewOrderStatus() {
        System.out.println("\n📋 Your Current Orders:");
        System.out.println("==========================================================================");
        System.out.printf("| %-8s | %-12s | %-15s | %-10s | %-15s |\n",
                "Order ID", "Order Type", "Total", "Status", "Date & Time");
        System.out.println("--------------------------------------------------------------------------");

        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // OrderID,CustomerID,CustomerName,Products,Quantities,Prices,Total,OrderType,PaymentMethod,Status,DateTime
                if (parts[1].equals(this.id) && !parts[9].equals("Completed") && !parts[9].equals("Cancelled")) {
                    found = true;
                    System.out.printf("| %-8s | %-12s | RM %-12s | %-10s | %-15s |\n",
                            parts[0], parts[7], parts[6], parts[9], parts[10]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading orders: " + e.getMessage());
        }

        if (!found) {
            System.out.println("|                    No active orders found                              |");
        }
        System.out.println("==========================================================================");
    }

    protected static void updateCustomerInfo(String[] user) {
        List<String> updatedLines = new ArrayList<>();
        String id = user[0];

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id)) {
                    parts[1] = user[1];
                    parts[2] = user[2];
                    parts[3] = user[3];
                    line = String.join(",", parts);
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        saveToFile(FILE, id + "," + name + "," + dateOfBirth + "," + phoneNumber);
    }

    public static void readCustomers() {
        System.out.println("📋 Customer List:");
        readFile(FILE);
    }
}



//==============================================================================================================




class Manager extends Person {
    private static final String FILE = System.getProperty("user.dir") + "/BakeryManagementSystem/src/managers.csv";
    private static final String ORDERS_FILE =System.getProperty("user.dir") + "/BakeryManagementSystem/src/orders.csv";

    public Manager(String name, String dateOfBirth, String phoneNumber) {
        super(name, dateOfBirth, phoneNumber);
        this.id = generateNewId(FILE, "M");
    }

    protected void showOrders() {
        System.out.println("\n📋 All Orders:");
        System.out.println("=============================================================================================================");
        System.out.printf("| %-8s | %-10s | %-15s | %-12s | %-12s | %-10s | %-15s |\n",
                "Order ID", "Customer", "Name", "Order Type", "Total", "Status", "Date & Time");
        System.out.println("-------------------------------------------------------------------------------------------------------------");

        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // OrderID,CustomerID,CustomerName,Products,Quantities,Prices,Total,OrderType,PaymentMethod,Status,DateTime
                System.out.printf("| %-8s | %-10s | %-15s | %-12s | RM %-9s | %-10s | %-15s |\n",
                        parts[0], parts[1], parts[2], parts[7], parts[6], parts[9], parts[10]);
            }
        } catch (IOException e) {
            System.out.println("No orders found.");
        }
        System.out.println("=============================================================================================================");
    }

    protected void updateOrderStatus() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter Order ID to update: ");
        String orderId = sc.next();

        System.out.println("\nSelect new status:");
        System.out.println("1. In Progress");
        System.out.println("2. Completed");
        System.out.print("Enter choice: ");
        String choice = sc.next();

        String newStatus = "";
        switch(choice) {
            case "1":
                newStatus = "In Progress";
                break;
            case "2":
                newStatus = "Completed";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(orderId)) {
                    parts[9] = newStatus; // Update status
                    line = String.join(",", parts);
                    found = true;
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (!found) {
            System.out.println("Order ID not found.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            System.out.println("✅ Order status updated to: " + newStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void cancelOrder() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter Order ID to cancel: ");
        String orderId = sc.next();

        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(orderId)) {
                    if (parts[9].equals("Completed")) {
                        System.out.println("Cannot cancel a completed order.");
                        updatedLines.add(line);
                        found = true;
                        continue;
                    }
                    parts[9] = "Cancelled";
                    line = String.join(",", parts);
                    found = true;
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (!found) {
            System.out.println("Order ID not found.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            System.out.println("✅ Order cancelled successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void updateManagerInfo(String[] user) {
        List<String> updatedLines = new ArrayList<>();
        String id = user[0];

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id)) {
                    parts[1] = user[1];
                    parts[2] = user[2];
                    parts[3] = user[3];
                    line = String.join(",", parts);
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        saveToFile(FILE, id + "," + name + "," + dateOfBirth + "," + phoneNumber);
    }

    public static void readManagers() {
        System.out.println("👔 Manager List:");
        readFile(FILE);
    }
}
