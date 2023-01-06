package aud10_1;

import java.util.*;
import java.util.stream.Collectors;

interface Subscriber {
    void updateVotes(int unit, String pollId, String party, int votes, int totalVotersPerPoll, int totalVotersPerUnit);
}

class ElectionUnit {

    int unit;
    Map<String, Integer> registeredVotesByPoll;
    List<Subscriber> subscribers;

    ElectionUnit(int unit, Map<String, Integer> votersByPoll) {
        this.unit = unit;
        this.registeredVotesByPoll = votersByPoll;
        subscribers = new ArrayList<>();
    }

    public void addVotes(String pollId, String party, int votes) {
        subscribers.forEach(subscriber -> subscriber.updateVotes(
                this.unit,
                pollId,
                party,
                votes,
                registeredVotesByPoll.get(pollId),
                registeredVotesByPoll.values().stream().mapToInt(i -> i).sum()
        ));
    }

    void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
}

class VotersTurnoutApp implements Subscriber {

    Map<Integer, Integer> castedVotes;
    Map<Integer, Integer> registeredVoters;

    VotersTurnoutApp() {
        castedVotes = new HashMap<>();
        registeredVoters = new HashMap<>();
    }

    @Override
    public void updateVotes(int unit, String pollId, String party, int votes, int totalVotersPerPoll, int totalVotersPerUnit) {
        registeredVoters.put(unit, totalVotersPerUnit);

        castedVotes.putIfAbsent(unit, 0);

        castedVotes.put(unit, castedVotes.get(unit) + votes);
//        castedVotes.computeIfPresent(unit, (k, v) -> {
//            v += votes;
//            return v;
//        }); java 8 solution
    }

    void printStatistics() {
        System.out.println("Unit: Casted: Voters: Turnout:");
        for (int unit : castedVotes.keySet()) {
            System.out.println(String.format("%10d %7d %7d %7.2f%%", unit, castedVotes.get(unit), registeredVoters.get(unit), (castedVotes.get(unit) * 100.0 / registeredVoters.get(unit))));
        }
    }
}

class SeatsApp implements Subscriber {

    Map<Integer, Map<String, Integer>> votesByPartyAndUnit;

    public SeatsApp() {
        votesByPartyAndUnit = new HashMap<>();
    }

    private Map<String, Integer> calculateSeats(Map<String, Integer> votesByParty) {
        int totalVotes = votesByParty.values().stream().mapToInt(i -> i).sum();
        int neededVotesPerSeat = totalVotes / 20;

        Map<String, Integer> results = new HashMap<>();

        votesByParty.forEach((party, votes) -> {
            int seats = votes / neededVotesPerSeat;
            results.put(party, seats);
        });

        int assignedSeats = results.values().stream().mapToInt(i -> i).sum();

        if (assignedSeats!=20){
            int diff = 20 - assignedSeats;

            int maxVotes = votesByParty.values().stream().mapToInt(i -> i).max().getAsInt();
            String partyWithMaxVotes = votesByParty.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue()==maxVotes)
                    .findFirst().get().getKey();

            results.put(partyWithMaxVotes, results.get(partyWithMaxVotes)+diff);

        }

        return results;
    }

    @Override
    public void updateVotes(int unit, String pollId, String party, int votes, int totalVotersPerPoll, int totalVotersPerUnit) {
        votesByPartyAndUnit.putIfAbsent(unit, new HashMap<>());
        votesByPartyAndUnit.get(unit).putIfAbsent(party, 0);

        votesByPartyAndUnit.get(unit).put(party, votesByPartyAndUnit.get(unit).get(party) + votes);

//        votesByPartyAndUnit.get(unit).computeIfPresent(party, (k,v) -> {
//            v+=votes;
//            return v;
//        });
    }

    void printStatistics () {

    }
}

class InvalidVotesException extends Exception {

}

class VotesController {
    List<String> parties;
    //K = IM, V = IE
    Map<String, Integer> unitPerPoll;
    Map<Integer, ElectionUnit> electionUnitMap;

    VotesController(List<String> parties, Map<String, Integer> unitPerPoll) {
        this.parties = parties;
        this.unitPerPoll = unitPerPoll;
        electionUnitMap = new HashMap<>();
    }

    void addElectionUnit(ElectionUnit electionUnit) {
        electionUnitMap.put(electionUnit.unit, electionUnit);
    }

    void addVotes(String pollId, String party, int votes) throws InvalidVotesException {
        if (!parties.contains(party)) {
            throw new InvalidVotesException();
        }
        int unitNumber = unitPerPoll.get(pollId);
        ElectionUnit electionUnit = electionUnitMap.get(unitNumber);
        electionUnit.addVotes(pollId, party, votes);
    }
}

public class ElectionAppTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> parties = Arrays.stream(sc.nextLine().split("\\s+")).collect(Collectors.toList());
        Map<String, Integer> unitPerPoll = new HashMap<>();
        Map<Integer, ElectionUnit> electionUnitMap = new TreeMap<>();

        int totalUnits = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < totalUnits; i++) {
            Map<String, Integer> votersPerPoll = new HashMap<>();
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            Integer unit = Integer.parseInt(parts[0]);
            for (int j = 1; j < parts.length; j += 2) {
                String pollId = parts[j];
                int voters = Integer.parseInt(parts[j + 1]);
                unitPerPoll.putIfAbsent(pollId, unit);
                votersPerPoll.put(pollId, voters);
            }

            electionUnitMap.putIfAbsent(unit, new ElectionUnit(unit, votersPerPoll));
        }
        VotesController controller = new VotesController(parties, unitPerPoll);

        electionUnitMap.values().forEach(controller::addElectionUnit);

        VotersTurnoutApp votersTurnoutApp = new VotersTurnoutApp();
        SeatsApp seatsApp = new SeatsApp();


        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String testCase = parts[0];

            if (testCase.equals("subscribe")) { //Example: subscribe votersTurnoutApp 1
                int unit = Integer.parseInt(parts[1]);
                String app = parts[2];
                if (app.equals("votersTurnoutApp")) {
                    electionUnitMap.get(unit).subscribe(votersTurnoutApp);
                } else {
                    electionUnitMap.get(unit).subscribe(seatsApp);
                }
            } else if (testCase.equals("unsubscribe")) { //Example: unsubscribe votersTurnoutApp 1
                int unit = Integer.parseInt(parts[1]);
                String app = parts[2];
                if (app.equals("votersTurnoutApp")) {
                    electionUnitMap.get(unit).unsubscribe(votersTurnoutApp);
                } else {
                    electionUnitMap.get(unit).unsubscribe(seatsApp);
                }
            } else if (testCase.equals("addVotes")) { // Example: addVotes 1234 A 1000
                String pollId = parts[1];
                String party = parts[2];
                int votes = Integer.parseInt(parts[3]);
                try {
                    controller.addVotes(pollId, party, votes);
                } catch (InvalidVotesException e) {
                    e.printStackTrace();
                }
            } else if (testCase.equals("printStatistics")) {
                String app = parts[1];
                if (app.equals("votersTurnoutApp")) {
                    System.out.println("PRINTING STATISTICS FROM VOTERS TURNOUT APPLICATION");
                    votersTurnoutApp.printStatistics();
                } else {
                    System.out.println("PRINTING STATISTICS FROM SEATS APPLICATION");
                    //seatsApp.printStatistics();
                }
            }
        }
    }
}
