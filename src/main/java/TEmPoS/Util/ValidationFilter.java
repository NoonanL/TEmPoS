package TEmPoS.Util;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ValidationFilter {

    private ArrayList<String> required;
    private JSONObject input;
    private Map<String, String> requiredMap;

    public ValidationFilter(Map<String,String> requiredMap, JSONObject input){
        this.requiredMap = requiredMap;
        this.input = input;
    }

    public ValidationFilter(Map<String,String> requiredMap, HttpServletRequest request){
        this.requiredMap = requiredMap;
        //this.request = request;
    }

    public static boolean authorizedRequest(HttpServletRequest request) {

        ArrayList<String> ipWhitelist = TextReader.getValidIpList();
        JSONObject input = null;

        if (!ipWhitelist.contains(request.getRemoteAddr())) {
            System.out.println("Rejected IP");
            return false;
        } else {
            System.out.println("IP Accepted");
//            try {
//                //System.out.println(request);
//                RequestJson requestParser = new RequestJson();
//                input = requestParser.parse(request);
//                System.out.println("JSON successfully parsed");
//                System.out.println(input);
//            } catch (Exception e) {
//                System.out.println("Error parsing JSON data from request: " + e);
//                return false;
//            }
//            for (Map.Entry<String, String> entry : requiredMap.entrySet())
//            {
//                //System.out.println(entry.getKey() + "/" + entry.getValue());
//                if(!input.has(entry.getKey())){
//                    System.out.println("Couldn't find key " + entry.getKey() + "!");
//                    return false;
//                }else{
//                    String testVal = input.getString(entry.getKey());
//                    switch(entry.getValue()) {
//                        case "String":
//                            break;
//                        case "double":
//                            try {
//                                //System.out.println("Testing value : " + testVal);
//                                Double.parseDouble(testVal);
//                            } catch (Exception e) {
//                                System.out.println(e);
//                                return false;
//                            }
//                            break;
//                        case "integer":
//                            try {
//                                Integer.parseInt(testVal);
//                            } catch (NumberFormatException e) {
//                                System.out.println(e);
//                                return false;
//                            }
//                            break;
//                    }
//                }
//                System.out.println("All required keys present");
//            }
            return true;
        }
    }


    /**
     * Checks that keys exist where expected and are of the specified type
     * @return true if all keys exist and are of the correct type
     */
    public static JSONObject isValid(HttpServletRequest request, Map<String, String> requiredMap){

        JSONObject output = null;
        try{
            RequestJson requestParser = new RequestJson();
            output = requestParser.parse(request);
        } catch (Exception e) {
            System.out.println("error parsing JSON from request: " + e);
        }

        if(output != null){
            for (Map.Entry<String, String> entry : requiredMap.entrySet())
            {
                //System.out.println(entry.getKey() + "/" + entry.getValue());
                if(!output.has(entry.getKey())){
                    System.out.println("Couldn't find key " + entry.getKey() + "!");
                    return null;
                }else{
                    String testVal = output.getString(entry.getKey());
                    switch(entry.getValue()){
                        case "String" :
                            break;
                        case "double" :
                            try{
                                //System.out.println("Testing value : " + testVal);
                                Double.parseDouble(testVal);
                            }catch(Exception e){
                                System.out.println(e);
                                return null;
                            }
                            break;
                        case "integer" :
                            try{
                                Integer.parseInt(testVal);
                            }catch(NumberFormatException e){
                                System.out.println(e);
                                return null;
                            }
                            break;
                    }
                }
            }
        }
        return output;
    }

}

