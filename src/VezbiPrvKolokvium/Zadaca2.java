import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

class WeatherStation {
    private int days;
    private List<WeatherMeasurement> weatherMeasurements;

    public WeatherStation(int days) {
        this.days = days;
        this.weatherMeasurements = new ArrayList<>();
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {
        if (!this.checkIfThereIsACloseDateToThis(date)) {
            this.deleleAllMeasurementsThatAreOlderXDaysFromTheCurrent(date);
            this.weatherMeasurements.add(new WeatherMeasurement(temperature, wind, humidity, visibility, date));
        }
    }

    public int total() {
        return this.weatherMeasurements.size();
    }

    public void status(Date from, Date to) throws RuntimeException {

        List<WeatherMeasurement> fromToWeatherMeasurements = new ArrayList<>();

        for (int i = 0; i < this.weatherMeasurements.size(); i++) {
            WeatherMeasurement currentWeatherMeasurement = this.weatherMeasurements.get(i);
            if (from.compareTo(currentWeatherMeasurement.getDate()) <= 0 && to.compareTo(currentWeatherMeasurement.getDate()) >= 0) {
                fromToWeatherMeasurements.add(currentWeatherMeasurement);
            }
        }

        if (fromToWeatherMeasurements.isEmpty()) {
            throw new RuntimeException();
        }

        Collections.sort(fromToWeatherMeasurements);

        float sumOfTemps = 0;
        for (WeatherMeasurement w: fromToWeatherMeasurements) {
            System.out.println(w);
            sumOfTemps += w.getTemperature();
        }
        System.out.println(String.format("Average temperature: %.2f", sumOfTemps * 1.0 / fromToWeatherMeasurements.size()));
    }

    private void deleleAllMeasurementsThatAreOlderXDaysFromTheCurrent(Date date) {
        for (int i = 0; i < this.weatherMeasurements.size(); i++) {
            long timeApart = date.getTime() - this.weatherMeasurements.get(i).getDate().getTime();
            long days = TimeUnit.MILLISECONDS.toDays(timeApart);
            if (days >= this.days) {
                this.weatherMeasurements.remove(i);
            }
        }
    }

    private boolean checkIfThereIsACloseDateToThis(Date date) {
        for (int i = 0; i < this.weatherMeasurements.size(); i++) {

            long timeApart = Math.abs(date.getTime() - this.weatherMeasurements.get(i).getDate().getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeApart);

            if (minutes <= 2.5) return true;
        }

        return false;
    }
}

class WeatherMeasurement implements Comparable<WeatherMeasurement> {
    private float temperature;
    private float wind;
    private float humidity;
    private float visibility;
    private Date date;

    public WeatherMeasurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public int compareTo(WeatherMeasurement o) {
        return this.date.compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", this.temperature, this.wind, this.humidity, this.visibility, this.date.toString());
    }
}