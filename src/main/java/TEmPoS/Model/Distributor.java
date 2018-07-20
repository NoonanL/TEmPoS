package TEmPoS.Model;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class Distributor {

    private String id;
    private String name;

    public Distributor(){
        this.id = "";
        this.name = "";
    }

    public Distributor(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        String EOLN = "\n";
        return this.id + EOLN +
                this.name + EOLN;
    }

    public JSONObject toJson(){
        JSONObject json;
        Map<String, String> distributor = new LinkedHashMap<>();

        distributor.put("id", this.getId());
        distributor.put("name", this.getName());
        json = new JSONObject(distributor);
        return json;
    }
}
