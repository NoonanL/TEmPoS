package TEmPoS.Util;

import java.io.BufferedWriter;
import java.io.FileWriter;

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



}
