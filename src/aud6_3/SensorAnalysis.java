package aud6_3;//package mk.ukim.finki.av6_3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class BadSensorException extends Exception {

    public BadSensorException(String sensorId) {
        super(String.format("No readings for sensor %s", sensorId));
    }
}

class BadMeasureException extends Exception {
    int timestamp;
    String sensorId;

    public BadMeasureException(int timestamp, String sensorId) {
        this.timestamp = timestamp;
        this.sensorId = sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    @Override
    public String getMessage() {
        return String.format("Error in timestamp: %d from sensor: %s", timestamp, sensorId);
    }
}

interface IGeo {
    double getLatitude();

    double getLongitude();

    default double distance(IGeo other) {
        return Math.sqrt(Math.pow(this.getLatitude() - other.getLatitude(), 2) + Math.pow(this.getLongitude() - other.getLongitude(), 2));
    }
}

class Measurement {
    int timestamp;
    double value;

    public Measurement(int timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public static Measurement createMeasurement(String data, String sensorId) throws BadMeasureException {
        String[] parts = data.split(":");
        int timestamp = Integer.parseInt(parts[0]);
        double value = Double.parseDouble(parts[1]);
        if (value < 0) {
            throw new BadMeasureException(timestamp, sensorId);
        }
        return new Measurement(timestamp, value);
    }
}

class Sensor {
    String sensorId;
    IGeo location;
    List<Measurement> measurements;

    public Sensor(String sensorId, IGeo location, List<Measurement> measurements) {
        this.sensorId = sensorId;
        this.location = location;
        this.measurements = measurements;
    }

    public static Sensor createSensor(String line) throws BadMeasureException, BadSensorException {
        //sensorId sensorLatitude sensorLongitude timestamp1:value1 timestamp2:value2 ...
        //2 2 4 6:3 7:9 8:4
        //0 1 2 3   4   5
        String[] parts = line.split("\\s+");
        String sensorId = parts[0];
        if (parts.length == 3) { //nema sensorski merenja
            throw new BadSensorException(sensorId);
        }


        IGeo location = new IGeo() {
            @Override
            public double getLatitude() {
                return Double.parseDouble(parts[1]);
            }

            @Override
            public double getLongitude() {
                return Double.parseDouble(parts[2]);
            }
        };

        List<Measurement> measurements = new ArrayList<>();
        long toSkip = 3;
        for (String part : parts) {
            if (toSkip > 0) {
                toSkip--;
                continue;
            }
            Measurement measurement = Measurement.createMeasurement(part, sensorId);
            measurements.add(measurement);
        }

        return new Sensor(sensorId, location, measurements);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorId='" + sensorId + '\'' +
                '}';
    }

    double averageValue() {
        return measurements.stream()
                .mapToDouble(m -> m.value)
                .average()
                .orElse(0);
    }

    double averageValue(IGeo iGeo, double dis, long from, long to) {
        if (this.location.distance(iGeo) >= dis) {
            return -1.0;
        }

        return measurements.stream()
                .filter(m -> m.timestamp >= from && m.timestamp <= to)
                .mapToDouble(m -> m.value)
                .average()
                .orElse(-1.0);
    }
}

class ExtremeValue {

}

class GeoSensorApplication {

    List<Sensor> sensors;

    GeoSensorApplication() {
        sensors = new ArrayList<>();
    }

    void readGeoSensors(Scanner scanner) throws BadSensorException, BadMeasureException {

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            sensors.add(Sensor.createSensor(line));
        }

    }

    public List<Sensor> inRange(IGeo location, double distance) {
        return sensors.stream()
                .filter(sensor -> sensor.location.distance(location) < distance)
                .collect(Collectors.toList());
    }

    public double averageValue() {
        return sensors.stream()
                .mapToDouble(Sensor::averageValue)
                .average().orElse(0);
    }


    public double averageDistanceValue(IGeo iGeo, double dis, long from, long to) {
        return sensors.stream()
                .mapToDouble(sensor -> sensor.averageValue(iGeo, dis, from, to))
                .filter(average -> average != -1.0)
                .average().getAsDouble();
    }

    public List<ExtremeValue> extremeValues(long t1, long t2) {
        return new ArrayList<>();
    }
}

public class SensorAnalysis {
    public static void main(String[] args) {
        GeoSensorApplication app = new GeoSensorApplication();

        Scanner s = new Scanner(System.in);
        double lat = s.nextDouble();
        double lon = s.nextDouble();
        double dis = s.nextDouble();
        long t1 = s.nextLong();
        long t2 = s.nextLong();

        s.nextLine();

        System.out.println("Access point on {" + lat + ", " + lon + "} distance:" + dis + " from:" + t1 + " - to:" + t2);

        try {
            app.readGeoSensors(s);


            System.out.println(app.inRange(new IGeo() {
                @Override
                public double getLatitude() {
                    return lat;
                }

                @Override
                public double getLongitude() {
                    return lon;
                }
            }, dis));

            System.out.println(app.averageValue());
            System.out.println(app.averageDistanceValue(new IGeo() {
                @Override
                public double getLatitude() {
                    return lat;
                }

                @Override
                public double getLongitude() {
                    return lon;
                }
            }, dis, t1, t2));

            System.out.println(app.extremeValues(t1, t2));
        } catch (BadSensorException e) {
            System.out.println(e.getMessage());
        } catch (BadMeasureException e) {
            System.out.println(e.getMessage());
        } finally {
            s.close();
        }
    }
}