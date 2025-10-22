import java.io.*;
import java.util.*;

public class Person {
    String name;
    String dateOfBirth;
    String id;
    String phoneNumber;

    public Person(String name, String dateOfBirth, String phoneNumber){
        this.dateOfBirth = dateOfBirth;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

}

class Customer extends Person{

    private static final String FILE = "customers.csv";

    public Customer(String name, String dateOfBirth, String id, String phoneNumber){
        super(name, dateOfBirth, phoneNumber);
        this.id = id;
    }

    public static void addCustomer(String name, String dateOfBirth, String id, String phoneNumber){
        try(FileWriter fw = new FileWriter(FILE, true)){
            fw.write(id + "," + name + "," + dateOfBirth + "," + phoneNumber);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}

class Manager extends Person{
    private static final String FILE = "managers.csv";

    public Manager(String name, String dateOfBirth, String id, String phoneNumber){
        super(name, dateOfBirth, phoneNumber);
        this.id = id;
    }

    public static void addManager(String name, String dateOfBirth, String id, String phoneNumber){
        try(FileWriter fw = new FileWriter(FILE, true)){
            fw.write(id + "," + name + "," + dateOfBirth + "," + phoneNumber);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
