import java.util.*;

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple

class Triple<T extends Number> {

    private List<T> numbers;

    public Triple(T... numbers) {
        this.numbers = new ArrayList<>(Arrays.asList(numbers));
    }

    public double max() {
        return this.numbers.stream()
                .mapToDouble(Number::doubleValue)
                .max()
                .getAsDouble();
    }

    public double average() {
        return this.numbers.stream().mapToDouble(Number::doubleValue).sum() / this.numbers.size();
    }

    public void sort() {
        this.numbers.sort(comparator);
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f",
                this.numbers.get(0).doubleValue(),
                this.numbers.get(1).doubleValue(),
                this.numbers.get(2).doubleValue());
    }

    public static Comparator<? super Number> comparator = (Comparator<Number>) (a, b) -> {
        if (a.doubleValue() > b.doubleValue()) {
            return 1;
        } else if (a.doubleValue() < b.doubleValue()) {
            return -1;
        }
        return 0;
    };
}

