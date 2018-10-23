package TEmPoS.Model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PurchaseOrder {

    private String id;
    private Map<String, String> products;
    private String branchId;
    private String status;
    private String UID;


    public PurchaseOrder(String id, Map<String, String> products, String branchId, String status, String uid) {
        this.id = id;
        this.products = products;
        this.branchId = branchId;
        this.status = status;
        UID = uid;
    }

    public PurchaseOrder() {
        this.id = "";
        this.products = new HashMap<>();
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

    public Map<String, String> getProducts() {
        return products;
    }

    public void setProducts(Map<String, String> products) {
        this.products = products;
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
                this.products.toString() + EOLN +
                this.branchId + EOLN +
                this.status + EOLN +
                this.UID + EOLN;
    }

    public JSONObject toJson(){
        JSONObject json;
        Map<String, String> purchaseOrder = new LinkedHashMap<>();
        purchaseOrder.put("id", this.getId());
        purchaseOrder.put("products", this.products.toString());
        purchaseOrder.put("branchId" , this.getBranchId());
        purchaseOrder.put("status" , this.getStatus());
        purchaseOrder.put("UID" , this.getUID());
        json = new JSONObject(purchaseOrder);
        return json;
    }
//
//
//
//    public JSONArray productsAsJson(){
//        JSONArray test = new JSONArray();
//        for(Product p : this.products){
//            //System.out.println(p.toString());
//            test.put(p.toJson());
//            //json = new JSONObject(product);
//        }
//        //JSONObject returnJson = new JSONObject(test);
//        //System.out.println("The product json array created by object is: " + test);
//        return test;
//    }
}
