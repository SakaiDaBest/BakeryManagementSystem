import java.util.Scanner;
import java.io.*;
import java.nio.Buffer;
import java.util.*;

public class BMS_FinalTech {
    static final String FILE_CUSTOMER = System.getProperty("user.dir") + "/BakeryManagementSystem/src/customers.csv";;
    static final String FILE_MANAGER = System.getProperty("user.dir") + "/BakeryManagementSystem/src/managers.csv";;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=====================================");
        System.out.println("     🍞 Bakery Management System");
        System.out.println("=====================================");

        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Login");
            System.out.println("2. Create User");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1-3): ");

            String choice = scanner.nextLine().trim();
            char state = 0;
            switch (choice) {
                case "1":
                    boolean loggedIn = false;
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
                    System.out.print("\nEnter a new Customer ID to create: ");
                    String newUserId = scanner.nextLine().trim();
                    if (newUserId.isEmpty()) {
                        System.out.println("⚠️  Customer ID cannot be empty.");
                    } else {
                        System.out.println("🎉 Customer '" + newUserId + "' created successfully!");
                    }
                    break;

                case "3":
                    System.out.println("\n👋 Exiting Bakery Management System...");
                    running = false;
                    break;

                default:
                    System.out.println("❌ Invalid choice. Please enter 1, 2, or 3.");
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
