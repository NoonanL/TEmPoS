package TEmPoS.Util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Logger {

    private Logger(){}

    private static String loginLog = "loginLog.txt";
    private static String requestLog = "requestLog.txt";


    private static void writeLog(String log, String filename){
        try {
            FileWriter fstream = new FileWriter(filename, true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(log + " " + new java.util.Date() + "\n");
            out.close();
        } catch (Exception e) {
            System.err.println("Error while writing to file: " +
                    e.getMessage());
        }
    }

    public static void login(String log){
        writeLog(log, loginLog);
    }

    public static void request(String log){
        writeLog(log, requestLog);
    }


    /**
     * Potential useful session stuff
     */
    private int sessionCount = 0;

    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        session.setMaxInactiveInterval(60);
        synchronized (this) {
            sessionCount++;
        }
        String id = session.getId();
        Date now = new Date();
        String message = "New Session created on " +
                now.toString() + "\nID: " + id + "\n" +
                "There are now " + "" + sessionCount +
                " live sessions in the application.";

        System.out.println(message);
    }

    public void sessionDestroyed(HttpSessionEvent se) {

        HttpSession session = se.getSession();
        String id = session.getId();
        synchronized (this) {
            --sessionCount;
        }
        String message = "Session destroyed"
                + "\nValue of destroyed session ID is" +
                "" + id +
                "\n" + "There are now " + "" + sessionCount +
                " live sessions in the application.";
        System.out.println(message);
    }



}
