package TEmPoS.Model;

import TEmPoS.db.H2Customer;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class Transaction {

    private String id;
    private String customerId;
    private String customerName;
    private String productId;
    private String productName;
    private String quantity;

    public Transaction() {
    }

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
        transaction.put("customerName", this.getCustomerName());
        transaction.put("productId" , this.getProductId());
        transaction.put("productName", this.getProductName());
        transaction.put("quantity", this.getQuantity());

        json = new JSONObject(transaction);
        return json;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
