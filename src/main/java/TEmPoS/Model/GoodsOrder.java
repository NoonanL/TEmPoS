package TEmPoS.Model;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class GoodsOrder {

    private String id;
    private String UID;
    private String productId;
    private String quantity;
    private String status;

    public GoodsOrder(){
        this.id = "";
        this.UID = "";
        this.productId = "";
        this.quantity = "";
        this.status = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JSONObject toJson(){
        JSONObject json;
        Map<String, String> goodsOrder = new LinkedHashMap<>();
        goodsOrder.put("id", this.getId());
        goodsOrder.put("UID" , this.getUID());
        goodsOrder.put("productId", this.getProductId());
        goodsOrder.put("quantity" , this.getQuantity());
        goodsOrder.put("status" , this.getStatus());
        json = new JSONObject(goodsOrder);
        return json;
    }


}
