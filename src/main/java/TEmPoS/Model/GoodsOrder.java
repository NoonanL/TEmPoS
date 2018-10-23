package TEmPoS.Model;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class GoodsOrder {

    private int id;
    private String UID;
    private int productId;
    private int quantity;

    public GoodsOrder(){
        this.id = 0;
        this.UID = "";
        this.productId = 0;
        this.quantity = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public JSONObject toJson(){
        JSONObject json;
        Map<String, String> goodsOrder = new LinkedHashMap<>();
        goodsOrder.put("id", String.valueOf(this.getId()));
        goodsOrder.put("UID" , this.getUID());
        goodsOrder.put("productId", String.valueOf(this.getProductId()));
        goodsOrder.put("quantty" , String.valueOf(this.getQuantity()));
        json = new JSONObject(goodsOrder);
        return json;
    }
}
