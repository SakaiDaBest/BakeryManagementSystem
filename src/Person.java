import java.io.*;
import java.util.*;

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

                switch (choice) {
                    case "1":
//wait ks/jh
                        break;
                    case "2":
//wait ks/jh
                        break;
                    case "3":
                        //wait ks/jh
                        break;
                    case "4":
                        updateInfoUI(type,user);
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
                System.out.println("2. Cancel Orders");
                System.out.println("3. View Customer List");
                System.out.println("4. View Manager List");
                System.out.println("5. View Sales Report");
                System.out.println("6. Update Product Stock");
                System.out.println("7. Add Product");
                System.out.println("8. Remove Product");
                System.out.println("9. Update Information");
                System.out.println("10. Log Out");

                choice = scanner.next();

                switch (choice) {
                    case "1":
                        //need wait jh
                        break;
                    case "2":
                        //need wait jh
                        break;
                    case "3":
                        Customer.readCustomers();
                        break;
                    case "4":
                        Manager.readManagers();
                        break;
                    case "5":
                        //ks
                        break;
                    case "6":
                        ProductManagement.editProduct();
                        break;
                    case "7":
                        ProductManagement.addProduct();
                        break;
                    case "8":
                        ProductManagement.deleteProduct();
                        break;
                    case "9":
                        updateInfoUI(type,user);
                        break;
                    case "10":
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
        boolean uExit = false;
        System.out.println("What would you like to update?");
        System.out.println("1.Name");
        System.out.println("2.Contact Number");
        System.out.println("3.Return");
        String uChoice = "";
        String change = "";
        uChoice = scanner.next();
        while(!uExit) {
            switch (uChoice) {
                case "1":
                    System.out.println("New Name: ");
                    change = scanner.next();
                    user[1] = change;
                    if(type=='C'){
                        Customer.updateCustomerInfo(user);
                    }else{
                        Manager.updateManagerInfo(user);
                    }
                    System.out.println("Update Successful!");
                    break;
                case "2":
                    System.out.println("New Contact Number: ");
                    change = scanner.next();
                    user[2] = change;
                    if(type=='C'){
                        Customer.updateCustomerInfo(user);
                    }else{
                        Manager.updateManagerInfo(user);
                    }
                    System.out.println("Update Successful!");
                    break;
                case "3":
                    uExit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        return user;
    }
}




//====================================================================================================




class Customer extends Person {
    private static final String FILE = "src/customers.csv";

    public Customer(String name, String dateOfBirth, String phoneNumber) {
        super(name, dateOfBirth, phoneNumber);
        this.id = generateNewId(FILE, "C");
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
            System.out.println("Customer information updated successfully!");
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
    private static final String FILE = "src/managers.csv";

    public Manager(String name, String dateOfBirth, String phoneNumber) {
        super(name, dateOfBirth, phoneNumber);
        this.id = generateNewId(FILE, "M");
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
            System.out.println("Manager information updated successfully!");
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
