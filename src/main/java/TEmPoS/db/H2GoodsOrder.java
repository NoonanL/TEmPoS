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
        String query = "INSERT into goodsOrder (UID, productId, quantity) VALUES(?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, goodsOrder.getUID());
            ps.setInt(2, goodsOrder.getProductId());
            ps.setInt(3, goodsOrder.getQuantity());
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


    private JSONObject parseOrders(ResultSet rs) throws SQLException {
        JSONObject orderList = new JSONObject();
        while (rs.next()) {
            GoodsOrder goodsOrder = new GoodsOrder();
            goodsOrder.setId(rs.getInt(1));
            goodsOrder.setUID(rs.getString(2));
            goodsOrder.setProductId(rs.getInt(3));
            goodsOrder.setQuantity(rs.getInt(4));
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
