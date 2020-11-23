import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;


class IntegerArrayTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        IntegerArray ia = null;
        switch (s) {
            case "testSimpleMethods":
                ia = new IntegerArray(generateRandomArray(scanner.nextInt()));
                testSimpleMethods(ia);
                break;
            case "testConcat":
                testConcat(scanner);
                break;
            case "testEquals":
                testEquals(scanner);
                break;
            case "testSorting":
                testSorting(scanner);
                break;
            case "testReading":
                testReading(new ByteArrayInputStream(scanner.nextLine().getBytes()));
                break;
            case "testImmutability":
                int a[] = generateRandomArray(scanner.nextInt());
                ia = new IntegerArray(a);
                testSimpleMethods(ia);
                testSimpleMethods(ia);
                IntegerArray sorted_ia = ia.getSorted();
                testSimpleMethods(ia);
                testSimpleMethods(sorted_ia);
                sorted_ia.getSorted();
                testSimpleMethods(sorted_ia);
                testSimpleMethods(ia);
                a[0] += 2;
                testSimpleMethods(ia);
                ia = ArrayReader.readIntegerArray(new ByteArrayInputStream(integerArrayToString(ia).getBytes()));
                testSimpleMethods(ia);
                break;
        }
        scanner.close();
    }

    static void testReading(InputStream in) {
        IntegerArray read = ArrayReader.readIntegerArray(in);
        System.out.println(read);
    }

    static void testSorting(Scanner scanner) {
        int[] a = readArray(scanner);
        IntegerArray ia = new IntegerArray(a);
        System.out.println(ia.getSorted());
    }

    static void testEquals(Scanner scanner) {
        int[] a = readArray(scanner);
        int[] b = readArray(scanner);
        int[] c = readArray(scanner);
        IntegerArray ia = new IntegerArray(a);
        IntegerArray ib = new IntegerArray(b);
        IntegerArray ic = new IntegerArray(c);
        System.out.println(ia.equals(ib));
        System.out.println(ia.equals(ic));
        System.out.println(ib.equals(ic));
    }

    static void testConcat(Scanner scanner) {
        int[] a = readArray(scanner);
        int[] b = readArray(scanner);
        IntegerArray array1 = new IntegerArray(a);
        IntegerArray array2 = new IntegerArray(b);
        IntegerArray concatenated = array1.concat(array2);
        System.out.println(concatenated);
    }

    static void testSimpleMethods(IntegerArray ia) {
        System.out.print(integerArrayToString(ia));
        System.out.println(ia);
        System.out.println(ia.sum());
        System.out.printf("%.2f\n", ia.average());
    }


    static String integerArrayToString(IntegerArray ia) {
        StringBuilder sb = new StringBuilder();
        sb.append(ia.length()).append('\n');
        for (int i = 0; i < ia.length(); ++i)
            sb.append(ia.getElementAt(i)).append(' ');
        sb.append('\n');
        return sb.toString();
    }

    static int[] readArray(Scanner scanner) {
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = scanner.nextInt();
        }
        return a;
    }


    static int[] generateRandomArray(int k) {
        Random rnd = new Random(k);
        int n = rnd.nextInt(8) + 2;
        int a[] = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = rnd.nextInt(20) - 5;
        }
        return a;
    }
}

final class IntegerArray {
    private final int[] ArrayList;


    IntegerArray(int a[]) {
        ArrayList = Arrays.copyOf(a, a.length);;
    }

    public int length() {
        return ArrayList.length;
    }

    public int getElementAt(int i) {
        return ArrayList[i];
    }

    public int sum() {
        return Arrays.stream(ArrayList).sum();
    }

    public double average() {
        return sum() * 1.0 / length();
    }

    public IntegerArray getSorted() {
        int[] newIntegerArray = Arrays.stream(ArrayList).sorted().toArray();
        return new IntegerArray(newIntegerArray);
    }

    public IntegerArray concat(IntegerArray ia) {
        int[] newIntegerArray = new int[ia.length() + length()];

        for (int i = 0; i < length(); i++) {
            newIntegerArray[i] = getElementAt(i);
        }

        for (int i = 0; i < ia.length(); i++) {
            newIntegerArray[ia.length() + i] = ia.getElementAt(i);
        }
        return new IntegerArray(newIntegerArray);
    }

    @Override
    public String toString() {
        String toString = "[";

        for (int i = 0; i < ArrayList.length; i++) {

            if (i != ArrayList.length - 1) {
                toString += String.format("%d, ", getElementAt(i));
            } else {
                toString += String.format("%d", getElementAt(i));
            }
        }

        toString += "]";

        return toString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerArray that = (IntegerArray) o;
        return Arrays.equals(ArrayList, that.ArrayList);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(ArrayList);
    }
}

class ArrayReader {
    public static IntegerArray readIntegerArray(InputStream input){
        Scanner sc = new Scanner(input);
        int n = sc.nextInt();
        int[] array = new int[n];
        for (int i=0; i<n; i++){
            array[i] = sc.nextInt();
        }
        sc.close();
        IntegerArray ia = new IntegerArray(array);
        return ia;
    }
}
