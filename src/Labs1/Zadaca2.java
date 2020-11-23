import java.util.*;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.Arrays;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.stream.IntStream;

class Bank {
    private String name;
    private LinkedHashMap<Long, Account> accounts;
    private double totalTransfers;
    private double totalProvision;

    public Bank(String name, Account[] accounts){
        this.accounts = Arrays.stream(accounts)
                .collect(Collectors.toMap(
                        Account::getId,
                        acc -> {
                            Account clone = acc;
                            try {
                                clone  = acc.clone();
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            return clone;
                        },
                        ((account, account2) -> account),
                        LinkedHashMap::new
                ));
        this.name = name;
        this.totalTransfers = 0;
        this.totalProvision = 0;
    }

    public boolean makeTransaction(Transaction t){
        if(!this.accounts.containsKey(t.getFromId()) || !this.accounts.containsKey(t.getToId()))
            return false;

        Account from = this.accounts.get(t.getFromId());
        Account to = this.accounts.get(t.getToId());

        if(!from.withdraw(t.getDoubleAmount() + t.getProvision()))
            return false;
        to.add(t.getDoubleAmount());

        this.totalProvision += t.getProvision();
        this.totalTransfers += t.getDoubleAmount();

        return true;
    }

    public String totalTransfers() {
        return Amount.toStringRepresentation(this.totalTransfers);
    }

    public String totalProvision() {
        return Amount.toStringRepresentation(this.totalProvision);
    }

    @Override
    public String toString() {
        String accounts = this.accounts.values().stream()
                .map(Account::toString)
                .collect(Collectors.joining());
        return String.format("Name: %s\n\n%s", this.name, accounts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        ArrayList<Account> thisAccounts = new ArrayList<>(this.accounts.values());
        ArrayList<Account> otherAccounts = new ArrayList<>(bank.accounts.values());
        return Double.compare(bank.totalTransfers, totalTransfers) == 0 &&
                Double.compare(bank.totalProvision, totalProvision) == 0 &&
                name.equals(bank.name) &&
                thisAccounts.size() == otherAccounts.size() &&
                IntStream.range(0, thisAccounts.size())
                        .allMatch(i -> thisAccounts.get(i).equals(otherAccounts.get(i)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accounts, totalTransfers, totalProvision);
    }

    public Account[] getAccounts(){
        return this.accounts.values().stream()
                .map(ac -> {
                    Account clone = ac;
                    try {
                        clone = ac.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    return clone;
                }).toArray(Account[]::new);
    }
}

class Account implements Cloneable{
    private long id;
    private String name;
    private Amount balance;

    public Account(String name, String balance) {
        this.id = new Random().nextLong();
        this.name = name;
        this.balance = new Amount(balance);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBalance() {
        return balance.getStringAmount();
    }

    public void setBalance(String balance) {
        this.balance = new Amount(balance);
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nBalance: %s\n", this.name, this.balance.getStringAmount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Account clone() throws CloneNotSupportedException {
        Account clone = (Account)super.clone();
        clone.balance = this.balance.clone();
        return clone;
    }

    public boolean withdraw(double amount){
        return this.balance.withdraw(amount);
    }

    public void add(double amount){
        this.balance.add(amount);
    }
}

class Amount implements Cloneable{

    private String stringAmount;
    private double doubleAmount;

    Amount(String amount) {
        this.stringAmount = amount;
        this.doubleAmount = toDoubleRepresentation(amount);
    }

    public static String toStringRepresentation(double doubleAmount){
        return String.format("%.2f$", doubleAmount).replace(",",".");
    }

    public static double toDoubleRepresentation(String stringAmount){
        return Double.parseDouble(stringAmount.substring(0, stringAmount.length() - 1));
    }

    public String getStringAmount() {
        return stringAmount;
    }

    public double getDoubleAmount() {
        return doubleAmount;
    }

    public boolean withdraw(double withdrawAmount){
        if(this.doubleAmount - withdrawAmount < 0)
            return false;
        this.doubleAmount = this.doubleAmount - withdrawAmount;
        this.stringAmount = toStringRepresentation(this.doubleAmount);
        return true;
    }

    public void add(double doubleAmount){
        this.doubleAmount += doubleAmount;
        this.stringAmount = toStringRepresentation(this.doubleAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return Double.compare(amount.doubleAmount, doubleAmount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doubleAmount);
    }

    @Override
    public Amount clone() throws CloneNotSupportedException {
        return (Amount) super.clone();
    }
}

abstract class Transaction {
    private long fromId;
    private long toId;
    protected String description;
    protected Amount amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = new Amount(amount);
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }


    public String getAmount() {
        return amount.getStringAmount();
    }
    public double getDoubleAmount(){ return  this.amount.getDoubleAmount(); };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return fromId == that.fromId &&
                toId == that.toId &&
                description.equals(that.description) &&
                amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, description, amount);
    }

    public abstract String getDescription();
    public abstract double getProvision();
}

class FlatAmountProvisionTransaction extends Transaction {
    private String flatProvision;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision = flatProvision;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public double getProvision() {
        return Amount.toDoubleRepresentation(this.flatProvision);
    }

    public String getFlatAmount() {
        return flatProvision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return flatProvision.equals(that.flatProvision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flatProvision);
    }
}

class FlatPercentProvisionTransaction extends Transaction {
    private int centsPerDollar;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int centsPerDollar) {
        super(fromId, toId, "FlatPercent", amount);
        this.centsPerDollar = centsPerDollar;
    }

    public int getPercent() {
        return centsPerDollar;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public double getProvision() {
        return ((int)this.amount.getDoubleAmount() * this.centsPerDollar)/100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return centsPerDollar == that.centsPerDollar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), centsPerDollar);
    }
}

class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1)&&!a3.equals(a1)&&!a4.equals(a1)&&!a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1)&&!fa2.equals(null)&&fa2.equals(fa1)&&fa1.equals(fa2)&&fa1.equals(fa3)&&!fa1.equals(fa4)&&!fa1.equals(fa5)&&!fa1.equals(fp1)&&fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}