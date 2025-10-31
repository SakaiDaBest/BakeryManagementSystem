import java.util.Scanner;
import java.io.*;
import java.nio.Buffer;
import java.util.*;

public class BMS_FinalTech {
    static final String FILE_CUSTOMER = System.getProperty("user.dir") + "/BakeryManagementSystem/src/customers.csv";;
    static final String FILE_MANAGER = System.getProperty("user.dir") + "/BakeryManagementSystem/src/managers.csv";;
    static final String MASTER_PASSWORD = "12345";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        boolean loggedIn = false;
        System.out.println("=====================================");
        System.out.println("     🍞 Bakery Management System");
        System.out.println("=====================================");

        while (running) {
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

                    boolean exit = false;
                    while(loggedIn == false){
                        System.out.print("\nEnter your User ID (or X to quit): ");
                        String userId = scanner.nextLine().trim();

                        String[] userINFO;

                        if (userId.isEmpty()) {
                            System.out.println("⚠️  User ID cannot be empty.");
                        } else {
                            if (userId.charAt(0) == 'M') {
                                state = 'M';
                                userINFO = authentication(FILE_MANAGER, userId);

                            }else if(userId.charAt(0)=='C'){
                                state = 'C';
                                userINFO = authentication(FILE_CUSTOMER, userId);
                            }else if(userId.charAt(0)=='X'){
                                exit = true;
                                break;
                            }else{
                                break;
                            }
                            if (userINFO.length == 2) {
                                loggedIn = true;
                            }
                        }
                    }
                    if(exit){
                        break;
                    }

                    if(state=='M'){

                    }else{

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
                    customer.save();
                    //auto login

                case "3":
                    System.out.print("\nEnter your Name: ");
                    String mname = scanner.next();
                    System.out.print("\nEnter your Date of Birth(DD/MM/YYYY): ");
                    String mdate = scanner.next();
                    System.out.print("\nEnter your Phone Number: ");
                    String mphoneNumber = scanner.next();
                    Manager manager = new Manager(mname, mdate, mphoneNumber);
                    manager.save();
                    //auto login



                case "4":
                    System.out.println("\n👋 Exiting Bakery Management System...");
                    running = false;
                    break;

                default:
                    System.out.println("❌ Invalid choice. Please enter 1, 2, 3 or 4.");
                    break;
            }
        }

        scanner.close();

    }

    protected static String[] authentication(String fileName, String userId){
        String pnInput = "";

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("\nEnter your phone number: ");
            pnInput = br.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }

        String myId = null;
        String myPhone = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                myId = line.split(",")[0];
                myPhone = line.split(",")[3];
                if(myId.equals(userId) && myPhone.equals(pnInput)){
                    System.out.println("\nLogin Successful");
                    return new String[]{myId,myPhone};
                }
            }

            System.out.println("\nLogin Failed. Please Ensure You Entered the Correct Credentials");
            return new String[0];

        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}
