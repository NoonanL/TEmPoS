package TEmPoS.Util;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class RequestJson {

    public RequestJson() {
    }

    public JSONObject parse(HttpServletRequest request) throws IOException {

        JSONObject output = null;

        // Read from request
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();

        reader.close();
        try{
            output = new JSONObject(data);
        }catch(Exception e){
            System.out.println("Error parsing json in requestJson.parse: " + e);
            System.out.println(data);
        }
        return output;


    }
}
