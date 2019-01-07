package TEmPoS.Model;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class Transaction {

    private String id;
    private String customerId;
    private String productId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public JSONObject toJson(){
        JSONObject json;
        Map<String, String> transaction = new LinkedHashMap<>();

        transaction.put("id", this.getId());
        transaction.put("customerId", this.getCustomerId());
        transaction.put("productId" , this.getProductId());

        json = new JSONObject(transaction);
        return json;
    }

}
