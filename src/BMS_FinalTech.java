import java.util.Scanner;
import java.io.*;
import java.util.*;

public class BMS_FinalTech {
    static final String FILE_CUSTOMER = System.getProperty("user.dir") + "/BakeryManagementSystem/src/customers.csv";;
    static final String FILE_MANAGER = System.getProperty("user.dir") + "/BakeryManagementSystem/src/managers.csv";;
    /* GeeksforGeeks. (2022, January 21). getproperty() and getproperties() methods of System Class in Java.
    from https://www.geeksforgeeks.org/java/getproperty-and-getproperties-methods-of-system-class-in-java/*/

    static final String MASTER_PASSWORD = "12345"; //used for creating new manager

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        System.out.println("=====================================");
        System.out.println("       Bakery Management System");
        System.out.println("=====================================");

        while (running) {
            boolean loggedIn = false;
            System.out.println("\nMain Menu:");
            System.out.println("1. Login");
            System.out.println("2. Create Customer");
            System.out.println("3. Create Manager");
            System.out.println("4. Exit");
            System.out.print("Choose an option (1-4): ");

            String choice = scanner.nextLine().trim();
            char state = 0;
            switch (choice) {
                case "1":
                    String[] userINFO = null;
                    boolean exit = false;
                    while(!loggedIn && !exit){
                        System.out.print("\nEnter your User ID (or X to quit): ");
                        String userId = scanner.nextLine().trim();

                        if (userId.isEmpty()) {
                            System.out.println("User ID cannot be empty.");
                            continue;
                        }

                        if (userId.charAt(0) == 'M') {//verify manager info
                            state = 'M';
                            userINFO = authentication(FILE_MANAGER, userId);
                        } else if(userId.charAt(0) == 'C'){//verify customer info
                            state = 'C';
                            userINFO = authentication(FILE_CUSTOMER, userId);
                        } else if(userId.charAt(0) == 'X' || userId.charAt(0) == 'x'){
                            exit = true;
                            break;
                        } else {
                            System.out.println("Invalid User ID format.");
                            continue;
                        }

                        if (userINFO.length == 4) {
                            loggedIn = true;
                            Person.showUI(state, userINFO);  //show UI after successful login
                        }
                    }
                    break;
                case "2":
                    System.out.print("\nEnter your Name: ");
                    String cname = scanner.next();
                    System.out.print("\nEnter your Date of Birth(DD/MM/YYYY): ");
                    String cdate = scanner.next();
                    System.out.print("\nEnter your Phone Number: ");
                    String cphoneNumber = scanner.next();
                    Customer customer = new Customer(cname, cdate, cphoneNumber);
                    customer.save(); //save new customer to customer.csv
                    state='C';
                    Person.showUI(state, customer.getInfo()); //shows customer UI after login
                    break;

                case "3":
                    System.out.print("\nEnter your Master Password: ");
                    String mpass = scanner.nextLine();
                    if(!mpass.equals(MASTER_PASSWORD)){
                        System.out.println("Master Password is Incorrect");
                        break;
                    }
                    System.out.print("\nEnter your Name: ");
                    String mname = scanner.nextLine();
                    System.out.print("\nEnter your Date of Birth(DD/MM/YYYY): ");
                    String mdate = scanner.nextLine();
                    System.out.print("\nEnter your Phone Number: ");
                    String mphoneNumber = scanner.nextLine();
                    Manager manager = new Manager(mname, mdate, mphoneNumber);
                    manager.save(); //save new customer to manager.csv
                    state='M';
                    Person.showUI(state, manager.getInfo()); //shows manager UI after login

                    break;


                case "4":
                    System.out.println("\nExiting Bakery Management System...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3 or 4.");
                    break;
            }
        }

        scanner.close();

    }

    protected static String[] authentication(String fileName, String userId){
        System.out.print("\nEnter your phone number: ");
        String pnInput = Person.scanner.nextLine().trim();

        String myId = null;
        String myPhone = null;
        String myName = null;
        String myDOB = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//            GeeksforGeeks. (2025, July 23). Difference Between BufferedReader and FileReader in Java.
//            from https://www.geeksforgeeks.org/java/difference-between-bufferedreader-and-filereader-in-java/
            String line;
            while ((line = reader.readLine()) != null) {
                myId = line.split(",")[0];
                myName = line.split(",")[1];
                myDOB = line.split(",")[2];
                myPhone = line.split(",")[3];
                if(myId.equals(userId) && myPhone.equals(pnInput)){
                    System.out.println("\nLogin Successful");
                    return new String[]{myId,myName,myDOB,myPhone};
                }
            }

            System.out.println("\nLogin Failed. Please Ensure You Entered the Correct Credentials");
            return new String[0];

        } catch (IOException e) {
            return new String[0];
        }
    }
}
