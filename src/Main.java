import java.util.Scanner;
import java.time.Instant;
import java.util.Date;

public class Main {
    private static String[][] stock;
    private static int stockCount;
    private static int[] catalogCounts;
    private static Scanner scanner = new Scanner(System.in);
    private static String[] history = new String[100];
    private static int historyCount = 0;

    public static void main(String[] args) {
        System.out.println("Welcome to Java Product Stock Management");
        System.out.println("=".repeat(40));

        while (true) {
            System.out.println("""
                1. Set up stock with catalogue
                2. View product in stock
                3. Insert product to stock catalogue
                4. Update product in stock catalogue by name
                5. Delete product in stock catalogue by name
                6. View insertion history
                7. Exit
                """);
            System.out.println("=".repeat(40));
            System.out.print("[+] Insert Option: ");
            int option = getValidatedInt();

            switch (option) {
                case 1 -> setUpStock();
                case 2 -> viewStock();
                case 3 -> insertProduct();
                case 4 -> updateProduct();
                case 5 -> deleteProduct();
                case 6 -> viewInsertionHistory();
                case 7 -> {
                    System.out.println("Exiting program...");
                    return;
                }
                default -> System.out.println("Invalid option! Please try again.");
            }

            pressEnterToContinue();
            System.out.println("=".repeat(40));
        }
    }

    private static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void setUpStock() {
        System.out.print("[+] Insert number of Stock: ");
        stockCount = getValidatedInt();

        stock = new String[stockCount][];
        catalogCounts = new int[stockCount];

        System.out.println("'Insert number of catalogue for each stock.'");
        for (int i = 0; i < stockCount; i++) {
            System.out.print("[+] Insert number of catalogue on stock [" + (i + 1) + "]: ");
            catalogCounts[i] = getValidatedInt();
            stock[i] = new String[catalogCounts[i]];
        }

        System.out.println("----- SET UP STOCK SUCCEEDED -----");
        for (int i = 0; i < stockCount; i++) {
            System.out.print("Stock [" + (i + 1) + "] => ");
            for (int j = 0; j < stock[i].length; j++) {
                System.out.print("[ " + (j + 1) + " - EMPTY ] ");
            }
            System.out.println();
        }

        addToHistory("Stock setup with " + stockCount + " stocks.");
    }

    private static void viewStock() {
        if (stock == null) {
            System.out.println("----------Stock not initialized--------");
            return;
        }

        for (int i = 0; i < stockCount; i++) {
            System.out.print("Stock [" + (i + 1) + "] => ");
            for (int j = 0; j < stock[i].length; j++) {
                String value = stock[i][j] == null ? "EMPTY" : stock[i][j];
                System.out.print("[ " + (j + 1) + " - " + value + " ] ");
            }
            System.out.println();
        }
    }

    private static void insertProduct() {
        if (stock == null) {
            System.out.println("Stock not initialized.");
            return;
        }

        System.out.print("[+] Enter stock number: ");
        int stockNum = getValidatedInt() - 1;
        System.out.print("[+] Enter catalogue number: ");
        int catalogNum = getValidatedInt() - 1;

        if (!isValidPosition(stockNum, catalogNum)) return;

        if (stock[stockNum][catalogNum] != null && !stock[stockNum][catalogNum].isEmpty()) {
            System.out.println("This position is already occupied by a product.");
            return;
        }

        System.out.print("[+] Enter product name: ");
        String name = scanner.nextLine();

        stock[stockNum][catalogNum] = name;
        String log = "Product '" + name + "' inserted at Stock[" + (stockNum + 1) + "][" + (catalogNum + 1) + "]";
        System.out.println(log);
        addToHistory(log);
    }

    private static void updateProduct() {
        if (stock == null) {
            System.out.println("Stock not initialized.");
            return;
        }

        System.out.print("[+] Enter product name to update: ");
        String target = scanner.nextLine();
        boolean found = false;

        for (int i = 0; i < stockCount; i++) {
            for (int j = 0; j < stock[i].length; j++) {
                if (target.equalsIgnoreCase(stock[i][j])) {
                    System.out.println("Found at Stock[" + (i + 1) + "][" + (j + 1) + "]");
                    System.out.print("[+] Enter new product name: ");
                    String newName = scanner.nextLine();
                    stock[i][j] = newName;
                    String log = "Updated '" + target + "' to '" + newName + "' at Stock[" + (i + 1) + "][" + (j + 1) + "]";
                    System.out.println(log);
                    addToHistory(log);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            System.out.println("Product not found.");
        }
    }

    private static void deleteProduct() {
        if (stock == null) {
            System.out.println("Stock not initialized.");
            return;
        }

        System.out.print("[+] Enter product name to delete: ");
        String target = scanner.nextLine();
        boolean found = false;

        for (int i = 0; i < stockCount; i++) {
            for (int j = 0; j < stock[i].length; j++) {
                if (target.equalsIgnoreCase(stock[i][j])) {
                    stock[i][j] = null;
                    String log = "Deleted product '" + target + "' from Stock[" + (i + 1) + "][" + (j + 1) + "]";
                    System.out.println(log);
                    addToHistory(log);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            System.out.println("Product not found.");
        }
    }

    private static void viewInsertionHistory() {
        boolean hasInsertion = false;
        System.out.println("Insert Product History:");
        for (int i = 0, count = 1; i < historyCount; i++) {
            if (history[i].toLowerCase().contains("inserted")) {
                System.out.println(count++ + ". " + history[i]);
                hasInsertion = true;
            }
        }
        if (!hasInsertion) {
            System.out.println("No insertions found in history.");
        }
    }


    private static boolean isValidPosition(int stockNum, int catalogNum) {
        if (stockNum < 0 || stockNum >= stockCount) {
            System.out.println("Invalid stock number.");
            return false;
        }
        if (catalogNum < 0 || catalogNum >= stock[stockNum].length) {
            System.out.println("Invalid catalogue number.");
            return false;
        }
        return true;
    }

    private static void addToHistory(String log) {
        if (historyCount < history.length) {
            Date date = Date.from(Instant.now());
            history[historyCount++] = "AT " + date + " - " + log;
        }
    }


    private static int getValidatedInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a valid number: ");
            }
        }
    }
}
