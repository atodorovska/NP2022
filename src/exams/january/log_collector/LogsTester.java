package exams.january.log_collector;

import java.util.*;
import java.util.stream.Collectors;

abstract class Log {
    String service;
    String microservice;
    String type;

    String message;
    long timestamp;

    public Log(String service, String microservice, String type, String message, long timestamp) {
        this.service = service;
        this.microservice = microservice;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }

    abstract int severity();

    public static Log createLog(String fullLog) {
        String[] parts = fullLog.split("\\s+");
        String service = parts[0];
        String microservice = parts[1];
        String type = parts[2];
        long timestamp = Long.parseLong(parts[parts.length - 1]);
        String message = Arrays.stream(parts).skip(3).limit(parts.length - 1).collect(Collectors.joining(" "));
        if (type.equalsIgnoreCase("info")) {
            return new InfoLog(service, microservice, type, message, timestamp);
        } else if (type.equalsIgnoreCase("warn")) {
            return new WarnLog(service, microservice, type, message, timestamp);
        } else {
            return new ErrorLog(service, microservice, type, message, timestamp);
        }
    }

    public String getService() {
        return service;
    }

    public String getMicroservice() {
        return microservice;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s|%s [%s] %s T:%d", service, microservice, type, message, timestamp);

    }
}


class InfoLog extends Log {

    public InfoLog(String service, String microservice, String type, String message, long timestamp) {
        super(service, microservice, type, message, timestamp);
    }

    @Override
    int severity() {
        return 0;
    }
}

class WarnLog extends Log {

    public WarnLog(String service, String microservice, String type, String message, long timestamp) {
        super(service, microservice, type, message, timestamp);
    }

    @Override
    int severity() {
        if (message.toLowerCase().contains("might cause error")) {
            return 2;
        } else {
            return 1;
        }
    }
}

class ErrorLog extends Log {

    public ErrorLog(String service, String microservice, String type, String message, long timestamp) {
        super(service, microservice, type, message, timestamp);
    }

    @Override
    int severity() {
        int res = 3;
        if (message.toLowerCase().contains("fatal")) {
            res += 2;
        }
        if (message.toLowerCase().contains("exception")) {
            res += 3;
        }
        return res;
    }
}

class Service {
    String name;
    Map<String, Microservice> microservices = new HashMap<>();

    public Service(String name) {
        this.name = name;
    }

    public void addLog(Log log) {
        microservices.putIfAbsent(log.microservice, new Microservice(log.microservice));
        microservices.get(log.microservice).addLog(log);
    }

    @Override
    public String toString() {
        IntSummaryStatistics allSeveritiesStats = microservices.values().stream().flatMap(microservice -> microservice.logs.stream()).mapToInt(Log::severity).summaryStatistics();

        return String.format("Service name: %s" + " Count of microservices: %d" + " Total logs in service: %d" + " Average severity for all logs: %.2f" + " Average number of logs per microservice: %.2f", name, microservices.size(), allSeveritiesStats.getCount(), allSeveritiesStats.getAverage(), allSeveritiesStats.getCount() / (float) microservices.size());
    }

    public double averageSeverityForAllLogs() {
        return microservices.values().stream().flatMap(microservice -> microservice.logs.stream()).mapToInt(Log::severity).average().orElse(0.0);
    }

    public Map<Integer, Integer> getSeverityDistribution(String microservice) {
        List<Integer> severities;
        if (microservice == null) {
            severities = microservices.values().stream().flatMap(m -> m.logs.stream().map(Log::severity)).collect(Collectors.toList());
        } else {
            severities = microservices.get(microservice).logs.stream().map(Log::severity).collect(Collectors.toList());
        }

        return severities.stream().collect(Collectors.groupingBy(i -> i, TreeMap::new, Collectors.summingInt(i -> 1)));
    }

    public void displayLogs(String microservice, String order) {
        Comparator<Log> byTimestamp = Comparator.comparing(Log::getTimestamp);
        Comparator<Log> bySeverity = Comparator.comparing(Log::severity).thenComparing(Log::getTimestamp);

        Comparator<Log> logComparator;

        switch (order) {
            case "NEWEST_FIRST":
                logComparator = byTimestamp.reversed();
                break;
            case "OLDEST_FIRST":
                logComparator = byTimestamp;
                break;
            case "MOST_SEVERE_FIRST":
                logComparator = bySeverity.reversed();
                break;
            default:
                logComparator = bySeverity;
        }

        List<Log> logs;
        if (microservice == null) {
            logs = microservices.values().stream().flatMap(ms -> ms.logs.stream()).collect(Collectors.toList());
        } else {
            logs = microservices.get(microservice).logs;
        }

        logs.stream().sorted(logComparator).forEach(System.out::println);
    }
}

class Microservice {
    String name;
    List<Log> logs = new ArrayList<>();

    public Microservice(String name) {
        this.name = name;
    }

    public void addLog(Log log) {
        logs.add(log);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

class LogCollector {
    Map<String, Service> services = new HashMap<>();

    public LogCollector() {

    }

    void addLog(String logFull) {
        Log log = Log.createLog(logFull);
        services.putIfAbsent(log.service, new Service(log.service));
        services.get(log.service).addLog(log);
    }

    void printServicesBySeverity() {
        services.values().stream().sorted(Comparator.comparing(Service::averageSeverityForAllLogs).reversed()).forEach(System.out::println);
    }

    Map<Integer, Integer> getSeverityDistribution(String service, String microservice) {
        return services.get(service).getSeverityDistribution(microservice);
    }

    void displayLogs(String service, String microservice, String order) {
        services.get(service).displayLogs(microservice, order);
    }
}

public class LogsTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogCollector collector = new LogCollector();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("addLog")) {
                collector.addLog(line.replace("addLog ", ""));
            } else if (line.startsWith("printServicesBySeverity")) {
                collector.printServicesBySeverity();
            } else if (line.startsWith("getSeverityDistribution")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                if (parts.length == 3) {
                    microservice = parts[2];
                }
                collector.getSeverityDistribution(service, microservice).forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
            } else if (line.startsWith("displayLogs")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                String order = null;
                if (parts.length == 4) {
                    microservice = parts[2];
                    order = parts[3];
                } else {
                    order = parts[2];
                }
                System.out.println(line);
                collector.displayLogs(service, microservice, order);
            }
        }
    }
}
