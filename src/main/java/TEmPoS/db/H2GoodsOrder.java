package TEmPoS.db;

import TEmPoS.Model.GoodsOrder;
import TEmPoS.Model.PurchaseOrder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2GoodsOrder extends H2Base{

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Customer.class);


    /**
     * establish connection to database
     * @param connectionSupplier     */
    public H2GoodsOrder(ConnectionSupplier connectionSupplier) {
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

    public boolean createGoodsOrder(GoodsOrder goodsOrder) throws SQLException {
        String query = "INSERT into goodsOrder (UID, productId, quantity, status) VALUES(?,?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, goodsOrder.getUID());
            ps.setString(2, goodsOrder.getProductId());
            ps.setString(3, goodsOrder.getQuantity());
            ps.setString(4, goodsOrder.getStatus());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editGoodsOrder(GoodsOrder goodsOrder){
        //System.out.println("Got to function");
        String query = "UPDATE goodsOrder SET productId =?, quantity=?, status=? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, goodsOrder.getProductId());
            ps.setString(2, goodsOrder.getQuantity());
            ps.setString(3, goodsOrder.getStatus());
            System.out.println(goodsOrder.getId());
            ps.setInt(4, Integer.parseInt(goodsOrder.getId()));
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getGoodsOrders(){
        final String GET_ORDERS_QUERY = "SELECT * FROM goodsOrder";
        JSONObject goodsOrders;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_ORDERS_QUERY)){
            ResultSet rs = ps.executeQuery();
            goodsOrders = parseOrders(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return goodsOrders;
    }

    public JSONObject getGoodsOrderByUid(String UID){
        final String GET_ORDER_BY_UID_QUERY = "SELECT * FROM goodsOrder WHERE UID=?";
        JSONObject poList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_ORDER_BY_UID_QUERY)){
            ps.setString(1, UID);
            ResultSet rs = ps.executeQuery();
            poList = parseOrders(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return poList;
    }

    public boolean existingOrder(String orderUID) throws SQLException{
        final String GET_ORDER_QUERY = "SELECT * FROM goodsOrder WHERE UID=?";
        try (PreparedStatement ps = getConnection().prepareStatement(GET_ORDER_QUERY)) {
            ps.setString(1, orderUID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    /**
     * deletes a customer record by their id
     * @param id the id of the customer to delete
     * @return boolean for success/failure
     */
    public boolean deleteGoodsOrderById(int id) {
        final String DELETE_ORDER_QUERY = "DELETE FROM goodsOrder WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_ORDER_QUERY)) {
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private JSONObject parseOrders(ResultSet rs) throws SQLException {
        JSONObject orderList = new JSONObject();
        while (rs.next()) {
            GoodsOrder goodsOrder = new GoodsOrder();
            goodsOrder.setId(rs.getString(1));
            goodsOrder.setUID(rs.getString(2));
            goodsOrder.setProductId(rs.getString(3));
            goodsOrder.setQuantity(rs.getString(4));
            goodsOrder.setStatus(rs.getString(5));
            orderList.put(String.valueOf(goodsOrder.getId()) , goodsOrder.toJson());
        }
        return orderList;
    }


    /**
     * BE CAREFUL WITH ME
     * Function to delete the customer table
     */
    public boolean deleteTable(){
        try (PreparedStatement ps = getConnection().prepareStatement("DROP TABLE goodsOrder")){
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
