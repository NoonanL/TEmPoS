package TEmPoS.Util;

import TEmPoS.Runner;
import org.json.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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


        ArrayList<String> ipWhitelist = Runner.ipWhiteList;

        if (!ipWhitelist.contains(request.getRemoteAddr())) {
            //System.out.println("Rejected IP");
            return false;
        } else {
            //System.out.println("IP Accepted");
            String secret = request.getHeader("secret");
            if(secret.equals("I am the server's secret!")){

                //Map<String, List<String>> headers = request.getHeaders();
                //Cookie cookie[] = request.getCookies();

               // for(Cookie s: cookie){
                    //System.out.println(s.getValue());
                    //if(s.)
                    //cookieVals.addAll(Arrays.asList(splitline));
                //}

                return true;
            }else{
                return false;
            }



        }

        /**
         * if header hash matches secret
         */
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

    public static String xssFilter(String message) {

        if (message == null)
            return (null);

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                default:
                    result.append(content[i]);
            }
        }
        return (result.toString());

    }

}

