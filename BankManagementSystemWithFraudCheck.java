import java.util.ArrayList;
import java.util.Scanner;

// Represents a single bank account, now tracking transaction history for fraud checks
class Account {
    private int accountNumber;
    private String accountHolderName;
    private double balance;
    private ArrayList<Double> withdrawalHistory = new ArrayList<>();
    private boolean flagged = false;

    public Account(int accountNumber, String accountHolderName, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<Double> getWithdrawalHistory() {
        return withdrawalHistory;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false; // insufficient funds
        }
        balance -= amount;
        withdrawalHistory.add(amount);
        return true;
    }

    public double averageWithdrawal() {
        if (withdrawalHistory.isEmpty()) return 0;
        double sum = 0;
        for (double w : withdrawalHistory) sum += w;
        return sum / withdrawalHistory.size();
    }

    public void display() {
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Account Holder : " + accountHolderName);
        System.out.println("Balance        : Rs. " + balance);
        System.out.println("Flagged        : " + (flagged ? "YES - under review" : "No"));
        System.out.println("-----------------------------");
    }
}

// Applies simple rule-based checks to decide if a transaction looks suspicious.
// This is a SIMULATION only - not a real fraud detection system.
class FraudDetector {
    private static final double LARGE_WITHDRAWAL_LIMIT = 50000.0;
    private static final int RAPID_WITHDRAWAL_COUNT = 3;
    private static final double SPIKE_MULTIPLIER = 5.0; // amount vs personal average

    // Returns a list of reasons a withdrawal looks suspicious (empty = looks fine)
    public ArrayList<String> checkWithdrawal(Account acc, double amount) {
        ArrayList<String> reasons = new ArrayList<>();

        // Rule 1: flat large-amount threshold
        if (amount > LARGE_WITHDRAWAL_LIMIT) {
            reasons.add("Amount exceeds the large-withdrawal limit of Rs. " + LARGE_WITHDRAWAL_LIMIT);
        }

        // Rule 2: sudden spike compared to this account's own average withdrawal
        double avg = acc.averageWithdrawal();
        if (avg > 0 && amount > avg * SPIKE_MULTIPLIER) {
            reasons.add("Amount is far above this account's usual withdrawal pattern (avg Rs. " + String.format("%.2f", avg) + ")");
        }

        // Rule 3: too many withdrawals already made in this session (proxy for "rapid" activity)
        if (acc.getWithdrawalHistory().size() >= RAPID_WITHDRAWAL_COUNT) {
            reasons.add("Multiple withdrawals made in a short session (count: " + acc.getWithdrawalHistory().size() + ")");
        }

        // Rule 4: withdrawing almost the entire balance in one go
        if (amount >= acc.getBalance() * 0.9 && acc.getBalance() > 0) {
            reasons.add("Withdrawal would drain nearly the entire account balance");
        }

        return reasons;
    }
}

// Manages the collection of accounts and operations on them
class Bank {
    private ArrayList<Account> accounts = new ArrayList<>();
    private int nextAccountNumber = 1001;
    private FraudDetector fraudDetector = new FraudDetector();

    public void createAccount(String name, double initialDeposit) {
        Account acc = new Account(nextAccountNumber, name, initialDeposit);
        accounts.add(acc);
        System.out.println("Account created successfully! Your Account Number is: " + nextAccountNumber);
        nextAccountNumber++;
    }

    private Account findAccount(int accNo) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accNo) {
                return acc;
            }
        }
        return null;
    }

    public void deposit(int accNo, double amount) {
        Account acc = findAccount(accNo);
        if (acc == null) {
            System.out.println("Account not found.");
            return;
        }
        acc.deposit(amount);
        System.out.println("Deposit successful. New balance: Rs. " + acc.getBalance());
    }

    public void withdraw(int accNo, double amount, Scanner sc) {
        Account acc = findAccount(accNo);
        if (acc == null) {
            System.out.println("Account not found.");
            return;
        }

        if (amount > acc.getBalance()) {
            System.out.println("Insufficient balance.");
            return;
        }

        // Run the fraud simulation BEFORE completing the withdrawal
        ArrayList<String> reasons = fraudDetector.checkWithdrawal(acc, amount);

        if (!reasons.isEmpty()) {
            acc.setFlagged(true);
            System.out.println("\n*** FRAUD CHECK WARNING ***");
            System.out.println("This transaction looks suspicious for the following reason(s):");
            for (String r : reasons) {
                System.out.println(" - " + r);
            }
            System.out.print("Do you want to proceed anyway? (yes/no): ");
            String confirm = sc.next();
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("Transaction cancelled.");
                return;
            }
            System.out.println("Proceeding with transaction under flagged status.");
        }

        acc.withdraw(amount);
        System.out.println("Withdrawal successful. New balance: Rs. " + acc.getBalance());
    }

    public void checkBalance(int accNo) {
        Account acc = findAccount(accNo);
        if (acc == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.println("Current balance: Rs. " + acc.getBalance());
    }

    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        System.out.println("===== All Accounts =====");
        for (Account acc : accounts) {
            acc.display();
        }
    }

    public void displayFlaggedAccounts() {
        boolean any = false;
        System.out.println("===== Flagged Accounts (Under Review) =====");
        for (Account acc : accounts) {
            if (acc.isFlagged()) {
                acc.display();
                any = true;
            }
        }
        if (!any) {
            System.out.println("No flagged accounts.");
        }
    }

    public void deleteAccount(int accNo) {
        Account acc = findAccount(accNo);
        if (acc == null) {
            System.out.println("Account not found.");
            return;
        }
        accounts.remove(acc);
        System.out.println("Account deleted successfully.");
    }
}

// Main class with the menu-driven console interface
public class BankManagementSystemWithFraudCheck {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();
        int choice;

        do {
            System.out.println("\n===== Simple Bank Management System (with Fraud-Check Simulation) =====");
            System.out.println("1. Create New Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Display All Accounts");
            System.out.println("6. Display Flagged Accounts");
            System.out.println("7. Delete Account");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    sc.nextLine(); // clear buffer
                    System.out.print("Enter Account Holder Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Initial Deposit Amount: ");
                    double initialDeposit = sc.nextDouble();
                    bank.createAccount(name, initialDeposit);
                    break;

                case 2:
                    System.out.print("Enter Account Number: ");
                    int depAccNo = sc.nextInt();
                    System.out.print("Enter Amount to Deposit: ");
                    double depAmount = sc.nextDouble();
                    bank.deposit(depAccNo, depAmount);
                    break;

                case 3:
                    System.out.print("Enter Account Number: ");
                    int wdAccNo = sc.nextInt();
                    System.out.print("Enter Amount to Withdraw: ");
                    double wdAmount = sc.nextDouble();
                    bank.withdraw(wdAccNo, wdAmount, sc);
                    break;

                case 4:
                    System.out.print("Enter Account Number: ");
                    int balAccNo = sc.nextInt();
                    bank.checkBalance(balAccNo);
                    break;

                case 5:
                    bank.displayAllAccounts();
                    break;

                case 6:
                    bank.displayFlaggedAccounts();
                    break;

                case 7:
                    System.out.print("Enter Account Number to Delete: ");
                    int delAccNo = sc.nextInt();
                    bank.deleteAccount(delAccNo);
                    break;

                case 8:
                    System.out.println("Thank you for using the Bank Management System!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);

        sc.close();
    }
}
    

