import java.util.Scanner;

public class BMS_FinalTech {
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

            switch (choice) {
                case "1":
                    System.out.print("\nEnter your User ID: ");
                    String userId = scanner.nextLine().trim();
                    if (userId.isEmpty()) {
                        System.out.println("⚠️  User ID cannot be empty.");
                    } else {
                        System.out.println("✅ Logged in as user: " + userId);
                    }
                    break;

                case "2":
                    System.out.print("\nEnter a new User ID to create: ");
                    String newUserId = scanner.nextLine().trim();
                    if (newUserId.isEmpty()) {
                        System.out.println("⚠️  User ID cannot be empty.");
                    } else {
                        System.out.println("🎉 User '" + newUserId + "' created successfully!");
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
}
