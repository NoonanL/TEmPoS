package TEmPoS.Model;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class PurchaseOrder {

    private String id;
    private String productId;
    private String SKU;
    private int quantity;
    private String branchId;
    private String status;
    private String UID;


    public PurchaseOrder(String id, String productId, String sku, int quantity, String branchId, String status, String uid) {
        this.id = id;
        this.productId = productId;
        this.SKU = sku;
        this.quantity = quantity;
        this.branchId = branchId;
        this.status = status;
        UID = uid;
    }

    public PurchaseOrder() {
        this.id = "";
        this.productId = "";
        this.SKU = "";
        this.quantity = 0;
        this.branchId = "";
        this.status = "";
        UUID uuid = UUID.randomUUID();
        this.UID = uuid.toString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString(){
        String EOLN = "\n";
        return this.id + EOLN +
                this.productId + EOLN +
                this.SKU + EOLN +
                this.quantity + EOLN +
                this.branchId + EOLN +
                this.status + EOLN +
                this.UID + EOLN;
    }

    public JSONObject toJson(){
        JSONObject json;
        Map<String, String> purchaseOrder = new LinkedHashMap<>();

        purchaseOrder.put("id", this.getId());
        purchaseOrder.put("productId", this.getProductId());
        purchaseOrder.put("SKU" , this.getSKU());
        purchaseOrder.put("quantity" , Integer.toString(this.getQuantity()));
        purchaseOrder.put("branchId" , this.getBranchId());
        purchaseOrder.put("status" , this.getStatus());
        purchaseOrder.put("UID" , this.getUID());
        json = new JSONObject(purchaseOrder);
        return json;
    }


}
