package TEmPoS.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ValidationFilter {

    public ValidationFilter(ArrayList<String> required, JSONObject input){

        for (Iterator it = input.keys(); it.hasNext();) {
            if(required.contains(it.next().toString())){
                System.out.println("Key found");
            }else{
                System.out.println("Key not found - error!");
            }
        }
    }


}

