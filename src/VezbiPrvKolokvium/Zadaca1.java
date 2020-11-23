import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class F1Test {

    public static void main(String[] args) throws ParseException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class F1Race {

    private List<F1Driver> f1Drivers;

    public F1Race() {
        this.f1Drivers = new ArrayList<>();
    }

    public void readResults(InputStream inputStream) throws ParseException {
        Scanner input = new Scanner(inputStream);

        while (input.hasNext()) {
            String driverDataInput = input.nextLine();
            String[] driverDataParts = driverDataInput.split(" ");
            this.f1Drivers.add(new F1Driver(driverDataParts[0], Arrays.copyOfRange(driverDataParts, 1, 4)));
        }
        input.close();
    }

    public void printSorted(OutputStream outputStream) {
        int position = 1;
        this.sortDrivers();
        for (F1Driver driver: this.f1Drivers) {
            System.out.println(driver.toString(position));
            position++;
        }
    }

    private void sortDrivers() {
        Collections.sort(this.f1Drivers);
    }
}

class F1Driver implements Comparable<F1Driver> {
    public String name;
    public String bestLap;

    public F1Driver(String name, String[] laps) throws ParseException {
        this.name = name;
        this.saveBestTime(laps);
    }

    private void saveBestTime(String[] laps) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SSS");

        String bestTime = laps[0];
        Date bestLapTime = sdf.parse(laps[0]);
        for (int i = 1; i < laps.length; i++) {
            if (bestLapTime.compareTo(sdf.parse(laps[i])) > 0) {
                bestTime = laps[i];
                bestLapTime = sdf.parse(laps[i]);
            }
        }
        this.bestLap = bestTime;
    }

    public String toString(int id) {
        return String.format("%d. %0$-11s %s", id, this.name, this.bestLap);
    }

    @Override
    public int compareTo(F1Driver o) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SSS");
        try {
            return sdf.parse(this.bestLap).compareTo(sdf.parse(o.bestLap));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }
}