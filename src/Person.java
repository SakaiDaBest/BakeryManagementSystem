import java.io.*;
import java.nio.Buffer;
import java.util.*;

public class Person {
    protected String name;
    protected String dateOfBirth;
    protected String id;
    protected String phoneNumber;

    Scanner scanner = new Scanner(System.in);

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

    public void showUI(char type){
        if(type=='C'){
            System.out.println("=====================================");
            System.out.println("     🍞      Customer Menu");
            System.out.println("=====================================");
            System.out.println("1. Make Order");
            System.out.println("2. View Order Status");
            System.out.println("3. View Order History");
            System.out.println("4. Log Out");

        }else if(type == 'M'){
            System.out.println("=====================================");
            System.out.println("     🍞       Manager Menu");
            System.out.println("=====================================");


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
