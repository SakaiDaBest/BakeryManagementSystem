import java.io.*;
import java.util.*;

// Abstract class for abstraction
abstract class Item {
    protected String name;
    protected double price;
    protected int stock;
    protected String category;

    public abstract void displayDetails();
}

// Product class — encapsulated fields + polymorphism via displayDetails()
class Product extends Item {
    private static int nextID = 1; // auto-increment ID
    private int id;

    public Product(String name, double price, int stock, String category) {
        this.id = nextID++;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public void displayDetails() {
        System.out.printf("| %-3d | %-12s | %-10s | %-10.2f | %-6d |\n",
                id, category, name, price, stock);
    }

    public String toCSV() {
        return category + "," + name + "," + price + "," + id + "," + stock;
    }
}

// Main program class
public class ProductManagement {
    private static final String FILE_NAME = System.getProperty("user.dir") + "/BakeryManagementSystem/src/Product.csv";
    private static Scanner sc = new Scanner(System.in);

    public ProductManagement() {
        File file = new File(FILE_NAME);
        try {
            if (!file.exists()) {
                FileWriter fw = new FileWriter(FILE_NAME);
                fw.write("Category,Name,Price,ID,Stock\n");
                fw.close();
            }
        } catch (IOException e) {
            System.out.println("Error initializing file.");
        }
    }

    // Add Product
    public static void addProduct() {
        String name = "";
        while (true) {
            System.out.print("Enter Product Name (letters only, >3 chars): ");
            name = sc.nextLine();
            if (name.matches("[a-zA-Z ]{3,}")) break;
            System.out.println("Invalid name! Use only letters and must be >3 characters.");
        }

        double price = 0;
        while (true) {
            System.out.print("Enter Price: ");
            try {
                price = Double.parseDouble(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: enter a number only.");
            }
        }

        int stock = 0;
        while (true) {
            System.out.print("Enter Stock: ");
            try {
                stock = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: enter an integer only.");
            }
        }

        String category = selectCategory();

        Product product = new Product(name, price, stock, category);
        try {
            RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw");

            if (raf.length() > 0) {
                raf.seek(raf.length() - 1);
                byte lastByte = raf.readByte();

                if (lastByte != '\n') {
                    raf.write('\n');
                }
            }

            raf.close();
        } catch (IOException e) {
            System.out.println("Error fixing newline before writing product.");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(product.toCSV());
            bw.newLine();
            System.out.println("Product added successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    // View Products (Table View)
    public static void viewProducts() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean first = true;

            System.out.println("\n========================================================================================");
            System.out.printf("| %-3s | %-12s | %-40s | %-10s | %-6s |\n",
                    "ID", "Category", "Name", "Price", "Stock");
            System.out.println("----------------------------------------------------------------------------------------");

            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",");

                // Validate that we have all required fields
                if (data.length < 5) {
                    System.out.println("⚠️ Skipping malformed line: " + line);
                    continue;
                }

                System.out.printf("| %-3s | %-12s | %-40s | %-10s | %-6s |\n",
                        data[3], data[0], data[1], data[2], data[4]);
            }

            System.out.println("========================================================================================");
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }

    // Edit Product
    public static void editProduct() {
        List<String[]> products = readAllProducts();
        if (products.size() == 0) {
            System.out.println("No products found.");
            return;
        }

        System.out.print("Enter Product ID to edit: ");
        String idInput = sc.nextLine();

        boolean found = false;

        for (String[] product : products) {
            if (product[3].equals(idInput)) {
                found = true;

                System.out.println("Editing Product ID: " + idInput);
                System.out.print("Enter new Name (leave blank to keep \"" + product[1] + "\"): ");
                String name = sc.nextLine();
                if (!name.isEmpty()) {
                    while (!name.matches("[a-zA-Z ]{3,}")) {
                        System.out.println("Invalid name! Use only letters and >3 characters.");
                        System.out.print("Enter new Name again: ");
                        name = sc.nextLine();
                    }
                    product[1] = name;
                }

                System.out.print("Enter new Price (leave blank to keep " + product[2] + "): ");
                String price = sc.nextLine();
                if (!price.isEmpty()) {
                    while (true) {
                        try {
                            Double.parseDouble(price);
                            product[2] = price;
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input: enter a number only.");
                            System.out.print("Enter new Price again: ");
                            price = sc.nextLine();
                        }
                    }
                }

                System.out.print("Enter new Stock (leave blank to keep " + product[4] + "): ");
                String stock = sc.nextLine();
                if (!stock.isEmpty()) {
                    while (true) {
                        try {
                            Integer.parseInt(stock);
                            product[4] = stock;
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input: enter an integer only.");
                            System.out.print("Enter new Stock again: ");
                            stock = sc.nextLine();
                        }
                    }
                }

                product[0] = selectCategory();

                System.out.println("Product updated successfully!");
                break;
            }
        }

        if (!found) {
            System.out.println("Product ID not found!");
        } else {
            writeAllProducts(products);
        }
    }

    // Delete Product
    public static void deleteProduct() {
        List<String[]> products = readAllProducts();
        if (products.size() == 0) {
            System.out.println("No products to delete.");
            return;
        }

        System.out.print("Enter Product ID to delete: ");
        String idInput = sc.nextLine();

        boolean found = false;
        Iterator<String[]> iterator = products.iterator();

        while (iterator.hasNext()) {
            String[] product = iterator.next();
            if (product[3].equals(idInput)) {
                iterator.remove();
                found = true;
                System.out.println("Product deleted successfully!");
                break;
            }
        }

        if (!found) {
            System.out.println("Product ID not found!");
        } else {
            writeAllProducts(products);
        }
    }
    public static String[] checkStock(int id, int amount){
        //Category,Name,Price,ID,Stock
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip header line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String category = parts[0];
                String name = parts[1];
                String price = parts[2];
                String sid = parts[3];
                String stock = parts[4];

                // Check if ID matches and stock is sufficient
                if(Integer.parseInt(sid) == id && Integer.parseInt(stock) >= amount){
                    return new String[]{category, name, price, sid, String.valueOf(amount)};
                }
            }

            // If we get here, either product not found or insufficient stock
            return new String[0];

        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        } catch (NumberFormatException e) {
            System.out.println("Error parsing product data.");
            e.printStackTrace();
            return new String[0];
        }
    }

    // Reduce stock for a product by ID
    public static boolean reduceStock(int id, int amount) {
        List<String[]> products = readAllProducts();
        if (products.size() == 0) {
            System.out.println("No products found.");
            return false;
        }

        boolean found = false;

        for (String[] product : products) {
            // product format: [Category, Name, Price, ID, Stock]
            if (product.length < 5) continue;

            try {
                int productId = Integer.parseInt(product[3]);
                int currentStock = Integer.parseInt(product[4]);

                if (productId == id) {
                    found = true;

                    if (currentStock < amount) {
                        System.out.println("Error: Insufficient stock! Current stock: " + currentStock);
                        return false;
                    }

                    // Reduce the stock
                    int newStock = currentStock - amount;
                    product[4] = String.valueOf(newStock);

                    System.out.println("Stock reduced successfully! New stock: " + newStock);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error parsing product data.");
                continue;
            }
        }

        if (!found) {
            System.out.println("Product ID not found!");
            return false;
        }

        writeAllProducts(products);
        return true;
    }

    // Utility: Read all products from CSV
    private static List<String[]> readAllProducts() {
        List<String[]> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] data = line.split(",");
                products.add(data);
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        return products;
    }

    // Utility: Write all products back to CSV
    private static void writeAllProducts(List<String[]> products) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write("Category,Name,Price,ID,Stock\n");
            for (String[] product : products) {
                bw.write(String.join(",", product));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    // Category selection
    private static String selectCategory() {
        while (true) {
            System.out.println("\nSelect Category:");
            System.out.println("1. Cake");
            System.out.println("2. Bread");
            System.out.println("3. Pastry");
            System.out.println("4. Pie");
            System.out.println("5. Cookie");
            System.out.print("Choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": return "Cake";
                case "2": return "Bread";
                case "3": return "Pastry";
                case "4": return "Pie";
                case "5": return "Cookie";
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Main Menu
    public static void main(String[] args) {
        ProductManagement pm = new ProductManagement();
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n=== Product Management System ===");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Edit Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter an integer.");
                continue;
            }

            switch (choice) {
                case 1 -> pm.addProduct();
                case 2 -> pm.viewProducts();
                case 3 -> pm.editProduct();
                case 4 -> pm.deleteProduct();
                case 5 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 5);
    }
}
