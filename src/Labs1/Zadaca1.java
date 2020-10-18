import java.util.*;
import java.util.stream.IntStream;

public class RomanConverterTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RomanConverter romanConverter = new RomanConverter();
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(romanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}

class RomanConverter implements IRomanConverter {
    private HashMap <Integer, List<String>> romanNumbersMap = new HashMap<>();
    private static final int valueToWhichWeCalculateByRomanNumbersMap = 3;

    RomanConverter() {
        romanNumbersMap.put(1, Arrays.asList("", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"));
        romanNumbersMap.put(2, Arrays.asList("", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"));
        romanNumbersMap.put(3, Arrays.asList("", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"));
    }

    public String toRoman(int n) {
        String romanNumber = "";
        int numberPositionCounter = 1;

        while (n > 0) {
            romanNumber = getRomanNumberForDecimalNumber(n % 10, numberPositionCounter) + romanNumber;
            n /= 10;
            numberPositionCounter++;
        }

        return romanNumber;
    }

    private String getRomanNumberForDecimalNumber(int n, int numberPositionCounter) {
        if (numberPositionCounter > valueToWhichWeCalculateByRomanNumbersMap) {
            return String.join("", Collections.nCopies(n, "M"));
        }
        return romanNumbersMap.get(numberPositionCounter).get(n);
    }
}

interface IRomanConverter {
    public String toRoman(int n);
}