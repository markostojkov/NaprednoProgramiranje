import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}

class DoubleMatrix {
    private final int rows;
    private final int columns;
    private final double[] arrayOfTheElements;
    private final double[][] matrixOfTheElements;

    DoubleMatrix(double array[], int rows, int columns) throws InsufficientElementsException {
        if (array.length < rows * columns) {
            throw new InsufficientElementsException();
        }

        this.rows = rows;
        this.columns = columns;
        this.arrayOfTheElements = Arrays.copyOfRange(array, array.length - rows * columns, array.length);
        this.matrixOfTheElements = new double[rows][columns];
        int count = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.matrixOfTheElements[i][j] = this.arrayOfTheElements[count];
                count++;
            }
        }
    }

    public String getDimensions() {
        return String.format("[%d x %d]", this.rows, this.columns);
    }

    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        row--;

        if (row >= this.rows || row < 0) {
            throw new InvalidRowNumberException();
        }

        double maxElement = this.matrixOfTheElements[row][0];

        for (int i = 1; i < this.columns; i++) {
            if (this.matrixOfTheElements[row][i] > maxElement) {
                maxElement = this.matrixOfTheElements[row][i];
            }
        }

        return maxElement;
    }

    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        column--;

        if (column >= this.columns || column < 0) {
            throw new InvalidColumnNumberException();
        }

        double maxElement = this.matrixOfTheElements[0][column];

        for (int i = 1; i < this.rows; i++) {
            if (this.matrixOfTheElements[i][column] > maxElement) {
                maxElement = this.matrixOfTheElements[i][column];
            }
        }

        return maxElement;
    }

    public double sum() {
        return Arrays.stream(this.arrayOfTheElements).sum();
    }

    public double[] toSortedArray() {
        double temp;
        double[] array = this.arrayOfTheElements;

        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length ; j++) {
                if (array[i] < array[j]) {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }

        return array;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns - 1; j++) {
                 stringBuilder.append(df.format(this.matrixOfTheElements[i][j]) + "\t");
            }
            stringBuilder.append(df.format(this.matrixOfTheElements[i][this.columns - 1]));

            if (i < this.rows - 1) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public int rows() {
        return this.rows;
    }

    public int columns() {
        return this.columns;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DoubleMatrix other = (DoubleMatrix)obj;
        if (!Arrays.deepEquals(this.matrixOfTheElements, other.matrixOfTheElements))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(this.matrixOfTheElements);
        return result;
    }
}

class MatrixReader {
    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {
        Scanner sc = new Scanner(input);
        int m = sc.nextInt();
        int n = sc.nextInt();
        double[] arr = new double[m*n];
        int i=0;
        while (sc.hasNextDouble()){
            arr[i++] = sc.nextDouble();
        }
        return new DoubleMatrix(arr, m, n);
    }
}

class InsufficientElementsException extends Exception {
    InsufficientElementsException(){
        super("Insufficient number of elements");
    }
}

class InvalidRowNumberException extends Exception {
    InvalidRowNumberException(){
        super("Invalid row number");
    }
}

class InvalidColumnNumberException extends Exception {
    InvalidColumnNumberException(){
        super("Invalid column number");
    }
}

