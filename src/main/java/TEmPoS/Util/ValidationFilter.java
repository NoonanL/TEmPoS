package TEmPoS.Util;

import org.json.JSONObject;

import java.util.ArrayList;

public class ValidationFilter {

    private ArrayList<String> required;
    private JSONObject input;

    public ValidationFilter(ArrayList<String> required, JSONObject input){
        this.required = required;
        this.input = input;
    }

    /**
     * Checks if there is a matching key for all fields in the required list.
     * @return true or false
     */
    public boolean isValid(){

        for(String s : required){
            if(!input.has(s)){
                System.out.println("Couldn't find key " + s + "!");
                return false;
            }
        }
        return true;
    }

}

