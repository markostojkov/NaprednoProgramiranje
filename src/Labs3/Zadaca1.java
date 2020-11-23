package Labs3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Zadaca1 {
}
import java.util.ArrayList;
        import java.util.List;
        import java.util.Scanner;

class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}

interface Item {
    int getPrice();

    String getName();
}

class PizzaValidItems {
    public static String StandardPizza = "Standard";
    public static String PepperoniPizza = "Pepperoni";
    public static String VegetarianPizza = "Vegetarian";

    public static int returnPriceForItem(String item) throws InvalidPizzaTypeException {
        if (item.equalsIgnoreCase(StandardPizza)) {
            return 10;
        }
        if (item.equalsIgnoreCase(PepperoniPizza)) {
            return 12;
        }
        if (item.equalsIgnoreCase(VegetarianPizza)) {
            return 8;
        }

        throw new InvalidPizzaTypeException("InvalidPizzaTypeException");
    }
}

class ExtraValidItems {
    public static String Coke = "Coke";
    public static String Ketchup = "Ketchup";

    public static int returnPriceForItem(String item) throws InvalidExtraTypeException {
        if (item.equalsIgnoreCase(Coke)) {
            return 5;
        }
        if (item.equalsIgnoreCase(Ketchup)) {
            return 3;
        }

        throw new InvalidExtraTypeException("InvalidExtraTypeException");
    }
}

class PizzaItem implements Item {
    private String type;
    private int price;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        this.price = PizzaValidItems.returnPriceForItem(type);
        this.type = type;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public String getName() {
        return this.type;
    }
}

class ExtraItem implements Item {
    private String type;
    private int price;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        this.price = ExtraValidItems.returnPriceForItem(type);
        this.type = type;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public String getName() {
        return this.type;
    }
}

class OrderItem {
    private Item item;
    private int count;

    OrderItem(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    Item getItem() {
        return this.item;
    }

    int getCount() {
        return this.count;
    }

    void setCount(int count) {
        this.count = count;
    }

    public String toString(int orderNumber) {
        return String.format("%0$3d.%0$-14s x %d  %0$3d$", orderNumber, this.item.getName(), this.count, this.item.getPrice() * this.count);
    }
}

class Order {
    private List<OrderItem> orderItems;
    private boolean isOrderLocked = false;

    Order() {
        this.orderItems = new ArrayList<>();
    }

    void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (this.isOrderLocked) throw new OrderLockedException();

        if (count > 10) throw new ItemOutOfStockException(item);

        boolean itemInList = false;

        for (int i = 0; i < this.orderItems.size(); i++) {
            if (item.getPrice() == this.orderItems.get(i).getItem().getPrice()) {
                this.orderItems.get(i).setCount(count);
                itemInList = true;
            }
        }

        if (!itemInList) {
            this.orderItems.add(new OrderItem(item, count));
        }
    }

    int getPrice() {
        int fullPrice = 0;

        for (int i = 0; i < this.orderItems.size(); i++) {
            fullPrice += this.orderItems.get(i).getCount() * this.orderItems.get(i).getItem().getPrice();
        }

        return fullPrice;
    }

    void displayOrder() {
        for (int i = 0; i < this.orderItems.size(); i++) {
            System.out.println(this.orderItems.get(i).toString(i + 1));
        }
        System.out.println(String.format("Total: %0$20d$", this.getPrice()));
    }

    void removeItem(int index) throws ArrayIndexOutOfBоundsException, OrderLockedException {
        if (this.isOrderLocked) throw new OrderLockedException();

        if (index + 1 > this.orderItems.size()) throw new ArrayIndexOutOfBоundsException(index);
        this.orderItems.remove(index);
    }

    void lock() throws EmptyOrder {
        if (this.orderItems.size() == 0) throw new EmptyOrder();

        this.isOrderLocked = true;
    }
}

class InvalidExtraTypeException extends Exception {
    InvalidExtraTypeException(String error) {
        super(error);
    }
}

class InvalidPizzaTypeException extends Exception {
    InvalidPizzaTypeException(String error) {
        super(error);
    }
}

class ItemOutOfStockException extends Exception {
    ItemOutOfStockException(Item item) {
        super();
    }
}

class ArrayIndexOutOfBоundsException extends Exception {
    ArrayIndexOutOfBоundsException(int index) {
        super();
    }
}

class EmptyOrder extends Exception {
    EmptyOrder() {
        super();
    }
}

class OrderLockedException extends Exception {
    OrderLockedException() {
        super();
    }
}