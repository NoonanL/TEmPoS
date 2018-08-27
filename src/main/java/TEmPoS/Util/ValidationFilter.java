package TEmPoS.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class ValidationFilter {

    private ArrayList<String> required;
    private JSONObject input;

    private Map<String, String> requiredMap;

    public ValidationFilter(ArrayList<String> required, JSONObject input){
        this.required = required;
        this.input = input;
    }

    public ValidationFilter(Map<String,String> requiredMap, JSONObject input){
        this.requiredMap = requiredMap;
        this.input = input;
    }
//
//    /**
//     * Checks if there is a matching key for all fields in the required list.
//     * @return true or false
//     */
//    public boolean isValid(){
//
//        for(String s : required){
//            if(!input.has(s)){
//                System.out.println("Couldn't find key " + s + "!");
//                return false;
//            }
//        }
//        return true;
//    }

    public boolean isValid(){

        for (Map.Entry<String, String> entry : requiredMap.entrySet())
        {
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            if(!input.has(entry.getKey())){
                System.out.println("Couldn't find key " + entry.getKey() + "!");
                return false;
            }else{
                String testVal = input.getString(entry.getKey());
                switch(entry.getValue()){
                    case "String" :
                        break;
                    case "double" :
                        try{
                            //System.out.println("Testing value : " + testVal);
                            Double.parseDouble(testVal);
                        }catch(Exception e){
                            System.out.println(e);
                            return false;
                        }
                        break;
                    case "integer" :
                        try{
                            Integer.parseInt(testVal);
                        }catch(NumberFormatException e){
                            System.out.println(e);
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

}

