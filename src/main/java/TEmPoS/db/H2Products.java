package TEmPoS.db;

import TEmPoS.Model.Product;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Products extends H2Base {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Customer.class);


    /**
     * establish connection to database
     * @param connectionSupplier     */
    public H2Products(ConnectionSupplier connectionSupplier) {
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

    public boolean createProduct(Product product){
        return true;
    }

    public boolean editProduct (int targetId, Product product){
        return true;
    }

    public JSONObject searchProducts(String searchString){
        JSONObject productList = null;
        return productList;
    }

    public JSONObject getAllProducts(){
        JSONObject productList = new JSONObject();
        return productList;
    }

    private JSONObject parseProducts(ResultSet rs){
        JSONObject productList = new JSONObject();
        return productList;
    }
    /**
     * BE CAREFUL WITH ME
     * Function to delete the customer table
     */
    public boolean deleteTable(){
        try (PreparedStatement ps = getConnection().prepareStatement("DROP TABLE products")){
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
