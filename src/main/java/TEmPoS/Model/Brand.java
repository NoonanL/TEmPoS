package TEmPoS.Model;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class Brand {

    private String id;
    private String brand;
    private String distributor;

    public Brand(){
        this.id = "";
        this.brand = "";
        this.distributor = "";
    }

    public Brand(String brand, String distributor){
        this. brand = brand;
        this.distributor = distributor;
    }

    public Brand(String id, String brand, String distributor){
        this.id = id;
        this. brand = brand;
        this.distributor = distributor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    @Override
    public String toString(){
        String EOLN = "\n";
        return this.id + EOLN +
                this.brand + EOLN +
                this.distributor + EOLN;
    }

    public JSONObject toJson(){
        JSONObject json;
        Map<String, String> brand = new LinkedHashMap<>();

        brand.put("id", this.getId());
        brand.put("brand", this.getBrand());
        brand.put("distributor" , this.getDistributor());
        json = new JSONObject(brand);
        return json;
    }
}
