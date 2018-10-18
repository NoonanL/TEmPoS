package TEmPoS.Util;

import java.io.*;
import java.util.ArrayList;

public class TextReader {


    private TextReader() {}


    public static ArrayList<String> getValidIpList() {
        ArrayList<String> validIpList = new ArrayList<>();
        try{
            File file = new File("whitelist.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null){
                validIpList.add(st);
            }
        }catch(IOException e){
            System.out.println("Error parsing IP list: " + e);
        }
        System.out.println("Accepting Traffic from the following IP addresses: " + validIpList.toString());
        return validIpList;
    }

}

