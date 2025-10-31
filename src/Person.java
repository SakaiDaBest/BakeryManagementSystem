import java.io.*;
import java.nio.Buffer;
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
                System.out.println("=====================================");
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

                        break;
                    case "2":

                        break;
                    case "3":

                        break;
                    case "4":

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
                System.out.println("=====================================");
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

                        break;
                    case "2":

                        break;
                    case "3":

                        break;
                    case "4":

                        break;
                    case "5":
                        //ks
                        break;
                    case "6":
                        //jh
                        break;
                    case "7":
                        //jh
                        break;
                    case "8":
                        //jh
                        break;
                    case "9":
                        //me
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
    }




//====================================================================================================




class Customer extends Person {
    private static final String FILE = "src/customers.csv";

    public Customer(String name, String dateOfBirth, String phoneNumber) {
        super(name, dateOfBirth, phoneNumber);
        this.id = generateNewId(FILE, "C");
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

    public void save() {
        saveToFile(FILE, id + "," + name + "," + dateOfBirth + "," + phoneNumber);
    }

    public static void readManagers() {
        System.out.println("👔 Manager List:");
        readFile(FILE);
    }
}
