package TEmPoS.db;

import TEmPoS.Model.PurchaseOrder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class H2PurchaseOrder extends H2Base {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Customer.class);


    /**
     * establish connection to database
     * @param connectionSupplier     */
    public H2PurchaseOrder(ConnectionSupplier connectionSupplier) {
        super(connectionSupplier.provide());
        try {
            initTable();
        } catch (Exception e) {
            LOG.error("Can't find database driver: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes SQL table, creates one if it doesn't already exist.
     */
    private void initTable() {
        loadResource("/schema.sql");
    }

    public boolean createPurchaseOrder(PurchaseOrder purchaseOrder) throws SQLException {
        String query = "INSERT into purchaseOrders (products, branchId, status, UID) VALUES(?,?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, purchaseOrder.getBranchId());
            ps.setString(2, purchaseOrder.getStatus());
            ps.setString(3, purchaseOrder.getUID());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getPurchaseOrders(){
        final String GET_ORDERS_QUERY = "SELECT * FROM purchaseOrders";
        JSONObject purchaseOrders;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_ORDERS_QUERY)){
            ResultSet rs = ps.executeQuery();
            purchaseOrders = parseOrders(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return purchaseOrders;
    }

    public JSONObject getPurchaseOrderByUID(int UID){
        final String GET_ORDER_BY_UID_QUERY = "SELECT * FROM purchaseOrders WHERE UID=?";
        JSONObject poList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_ORDER_BY_UID_QUERY)){
            ps.setInt(1, UID);
            ResultSet rs = ps.executeQuery();
            poList = parseOrders(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return poList;
    }

//    public boolean editPurchaseOrder(PurchaseOrder purchaseOrder) throws SQLException {
//        String query = "UPDATE purchaseOrders SET products =?, branchId =?, status =? WHERE id=?";
//        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
//            ps.setString(1, purchaseOrder.productsAsJson().toString());
//            ps.setString(2, purchaseOrder.getBranchId());
//            ps.setString(3, purchaseOrder.getStatus());
//            ps.setInt(4, Integer.parseInt(purchaseOrder.getId()));
//            ps.execute();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean existingOrder(String orderUID) throws SQLException{
        final String GET_ORDER_QUERY = "SELECT * FROM purchaseOrders WHERE UID=?";
        try (PreparedStatement ps = getConnection().prepareStatement(GET_ORDER_QUERY)) {
            ps.setString(1, orderUID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    private JSONObject parseOrders(ResultSet rs) throws SQLException {
        JSONObject orderList = new JSONObject();
        while (rs.next()) {
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setId(rs.getString(1));
            purchaseOrder.setBranchId(rs.getString(2));
            purchaseOrder.setStatus(rs.getString(4));
            purchaseOrder.setUID(rs.getString(5));
            orderList.put(purchaseOrder.getId() , purchaseOrder.toJson());
        }
        return orderList;
    }



    /**
     * BE CAREFUL WITH ME
     * Function to delete the customer table
     */
    public boolean deleteTable(){
        try (PreparedStatement ps = getConnection().prepareStatement("DROP TABLE purchaseOrders")){
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
