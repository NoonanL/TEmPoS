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

    public boolean isValid(){

        for(String s : required){
            if(!input.has(s)){
                System.out.println("Couldn't find key!");
                return false;
            }
        }
        return true;
    }

}

