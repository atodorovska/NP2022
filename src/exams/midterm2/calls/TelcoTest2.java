package exams.midterm2.calls;


import java.util.*;
import java.util.stream.Collectors;

class DurationConverter {
    public static String convert(long duration) {
        long minutes = duration / 60;
        duration %= 60;
        return String.format("%02d:%02d", minutes, duration);
    }
}

class Call {
    String uuid;
    String dialer;
    String receiver;
    public CallState state;

    public Call(String uuid, String dialer, String receiver, long initialized) {
        this.uuid = uuid;
        this.dialer = dialer;
        this.receiver = receiver;
        state = new InitializedState(this);
        state.initialized = initialized;
    }

    public void updateCall(long timestamp, String state) throws InvalidOperation {
        if (state.equals("ANSWER")) {
            this.state.answer(timestamp);
        } else if (state.equals("END")) {
            this.state.end(timestamp);
        } else if (state.equals("HOLD")) {
            this.state.hold(timestamp);
        } else if (state.equals("RESUME")) {
            this.state.resume(timestamp);
        }
    }

    public long getStart() {
        return state.start == 0 ? state.initialized : state.start;
    }

    public String getUuid() {
        return uuid;
    }

    long getTotalDuration() {
        return state.getTotalDuration();
    }
}

interface ICallState {
    void answer(long timestamp) throws InvalidOperation;

    void end(long timestamp) throws InvalidOperation;

    void hold(long timestamp) throws InvalidOperation;

    void resume(long timestamp) throws InvalidOperation;
}

abstract class CallState implements ICallState {
    Call call;
    public long initialized;
    public long start;
    public long end;
    public long holdStarted;
    public long holdEnded;
    public int durationHold;

    public CallState(Call call) {
        this.call = call;
    }

    public CallState(CallState oldState) {
        this.initialized = oldState.initialized;
        this.start = oldState.start;
        this.end = oldState.end;
        this.holdStarted = oldState.holdStarted;
        this.holdEnded = oldState.holdEnded;
        this.durationHold = oldState.durationHold;
        this.call = oldState.call;
    }

    public long getTotalDuration() {
        return end == 0 ? 0 : (end - start - durationHold);
    }

    void endHold(long timestamp) {
        this.holdEnded = timestamp;
        this.durationHold += (holdEnded - holdStarted);
        this.holdStarted = 0;
        this.holdEnded = 0;
    }
}

class InvalidOperation extends Exception {

}

class InitializedState extends CallState {

    public InitializedState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        this.start = timestamp;
        call.state = new InProgressState(this);

    }

    @Override
    public void end(long timestamp) {
        this.start = timestamp;
        call.state = new IdleState(this);

    }

    @Override
    public void hold(long timestamp) throws InvalidOperation {
        throw new InvalidOperation();
    }

    @Override
    public void resume(long timestamp) throws InvalidOperation {
        throw new InvalidOperation();
    }
}

class InProgressState extends CallState {

    public InProgressState(CallState callState) {
        super(callState);
    }

    @Override
    public void answer(long timestamp) throws InvalidOperation {
        throw new InvalidOperation();
    }

    @Override
    public void end(long timestamp) {
        this.end = timestamp;
        call.state = new IdleState(this);
    }

    @Override
    public void hold(long timestamp) {
        this.holdStarted = timestamp;
        call.state = new PausedState(this);
    }

    @Override
    public void resume(long timestamp) throws InvalidOperation {
        throw new InvalidOperation();
    }
}

class PausedState extends CallState {

    public PausedState(CallState callState) {
        super(callState);
    }

    @Override
    public void answer(long timestamp) throws InvalidOperation {
        throw new InvalidOperation();
    }

    @Override
    public void end(long timestamp) {
        this.endHold(timestamp);
        this.end = timestamp;
        call.state = new IdleState(this);
    }

    @Override
    public void hold(long timestamp) throws InvalidOperation {
        throw new InvalidOperation();
    }

    @Override
    public void resume(long timestamp) {
        this.endHold(timestamp);
        call.state = new InProgressState(this);
    }
}

class IdleState extends CallState {

    public IdleState(CallState callState) {
        super(callState);
    }

    @Override
    public void answer(long timestamp) throws InvalidOperation {
        throw new InvalidOperation();
    }

    @Override
    public void end(long timestamp) throws InvalidOperation {
        throw new InvalidOperation();
    }

    @Override
    public void hold(long timestamp) throws InvalidOperation {
        throw new InvalidOperation();
    }

    @Override
    public void resume(long timestamp) throws InvalidOperation {
        throw new InvalidOperation();
    }
}

class TelcoApp {
    Map<String, Call> callsByUuid = new HashMap<>();
    Map<String, List<Call>> callsByPhoneNumber = new HashMap<>();
    Comparator<Call> byStart = Comparator.comparing(Call::getStart).thenComparing(Call::getUuid);
    Comparator<Call> byDuration = Comparator.comparing(Call::getTotalDuration).thenComparing(Call::getStart).reversed();

    void addCall(String uuid, String dialer, String receiver, long timestamp) {
        Call c = new Call(uuid, dialer, receiver, timestamp);
        callsByUuid.put(uuid, c);
        callsByPhoneNumber.putIfAbsent(dialer, new ArrayList<>());
        callsByPhoneNumber.get(dialer).add(c);
        callsByPhoneNumber.putIfAbsent(receiver, new ArrayList<>());
        callsByPhoneNumber.get(receiver).add(c);
    }

    void updateCall(String uuid, long timestamp, String action) {
        try {
            callsByUuid.get(uuid).updateCall(timestamp, action);
        } catch (InvalidOperation e) {
            System.out.println("Invalid operation " + action + " for call " + uuid);
        }
    }

    void printCall(Call c, String phoneNumber) {
        String type = c.dialer.equals(phoneNumber) ? "D" : "R";
        String otherPhoneNumber = c.dialer.equals(phoneNumber) ? c.receiver : c.dialer;
        String end = c.state.end == 0 ? "MISSED CALL" : String.valueOf(c.state.end);
        System.out.println(String.format("%s %s %d %s %s", type, otherPhoneNumber, c.getStart(), end, DurationConverter.convert(c.getTotalDuration())));

    }

    void printChronologicalReport(String phoneNumber) {
        callsByPhoneNumber.get(phoneNumber).stream().sorted(byStart).forEach(c -> printCall(c, phoneNumber));
    }

    void printReportByDuration(String phoneNumber) {
        callsByPhoneNumber.get(phoneNumber).stream().sorted(byDuration).forEach(c -> printCall(c, phoneNumber));
    }

    public void printCallsDuration() {
        TreeMap<String, Long> result = callsByUuid.values().stream().collect(Collectors.groupingBy(
                c -> String.format("%s <-> %s", c.dialer, c.receiver),
                TreeMap::new,
                Collectors.summingLong(Call::getTotalDuration)
        ));

        result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.printf("%s : %s%n", entry.getKey(), DurationConverter.convert(entry.getValue())));
    }
}


public class TelcoTest2 {
    public static void main(String[] args) {
        TelcoApp app = new TelcoApp();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String command = parts[0];

            if (command.equals("addCall")) {
                String uuid = parts[1];
                String dialer = parts[2];
                String receiver = parts[3];
                long timestamp = Long.parseLong(parts[4]);
                app.addCall(uuid, dialer, receiver, timestamp);
            } else if (command.equals("updateCall")) {
                String uuid = parts[1];
                long timestamp = Long.parseLong(parts[2]);
                String action = parts[3];
                app.updateCall(uuid, timestamp, action);
            } else if (command.equals("printChronologicalReport")) {
                String phoneNumber = parts[1];
                app.printChronologicalReport(phoneNumber);
            } else if (command.equals("printReportByDuration")) {
                String phoneNumber = parts[1];
                app.printReportByDuration(phoneNumber);
            } else {
                app.printCallsDuration();
            }
        }

    }
}

