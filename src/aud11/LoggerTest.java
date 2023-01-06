package aud11;

class Logger { //Singleton
    static Logger instance;
    StringBuilder sb; //shared memory

    private Logger () {
        sb = new StringBuilder();
    }

    public synchronized void addLog (String log) {
        sb.append(log).append("\n");
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    public static synchronized Logger getInstance() {
        if (instance==null){
            instance = new Logger();
        }
        return instance;
    }
}

public class LoggerTest {
    public static void main(String[] args) {

        Logger l1 = Logger.getInstance();

        Logger l2 = Logger.getInstance();

        for (int i=0;i<10;i++){
            String logStatement = String.format("Log: %d", i);
            if (i%2==0){
                l1.addLog(logStatement);
            } else {
                l2.addLog(logStatement);
            }
        }

        System.out.println("LOGGER 1");
        System.out.println(l1);
        System.out.println("LOGGER 2");
        System.out.println(l2);
    }
}
