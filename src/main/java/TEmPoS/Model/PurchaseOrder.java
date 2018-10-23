package TEmPoS.Model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PurchaseOrder {

    private String id;
    private String branchId;
    private String status;
    private String UID;


    public PurchaseOrder(String id, String branchId, String status, String uid) {
        this.id = id;
        this.branchId = branchId;
        this.status = status;
        UID = uid;
    }

    public PurchaseOrder() {
        this.id = "";
        this.branchId = "";
        this.status = "";
        this.UID = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }


    @Override
    public String toString(){
        String EOLN = "\n";
        return this.id + EOLN +
                this.branchId + EOLN +
                this.status + EOLN +
                this.UID + EOLN;
    }

    public JSONObject toJson(){
        JSONObject json;
        Map<String, String> purchaseOrder = new LinkedHashMap<>();
        purchaseOrder.put("id", this.getId());
        purchaseOrder.put("branchId" , this.getBranchId());
        purchaseOrder.put("status" , this.getStatus());
        purchaseOrder.put("UID" , this.getUID());
        json = new JSONObject(purchaseOrder);
        return json;
    }

}
