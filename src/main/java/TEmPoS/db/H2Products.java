package TEmPoS.db;

import TEmPoS.Model.Customer;
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
        String query = "INSERT into products (SKU,name,RRP,cost,department,brand,description) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, product.getSKU());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getRRP());
            ps.setDouble(4, product.getCost());
            ps.setString(5, product.getDepartment());
            ps.setString(6, product.getBrand());
            ps.setString(7, product.getDescription());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editProduct (int targetId, Product product){
        final String EDIT_USER_QUERY = "UPDATE products SET SKU =?, name=?, RRP=?, cost=?, department=?, brand=?, description=? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(EDIT_USER_QUERY)) {
            ps.setString(1, product.getSKU());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getRRP());
            ps.setDouble(4, product.getCost());
            ps.setString(5, product.getDepartment());
            ps.setString(6, product.getBrand());
            ps.setString(7, product.getDescription());
            ps.setInt(8, targetId);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject searchProducts(String searchString){
        final String SEARCH_PRODUCTS_QUERY = "SELECT * FROM products WHERE id LIKE ? OR SKU LIKE ? OR name LIKE ?";
        JSONObject productList;
        String searchWildcard = "%" + searchString + "%";
        try (PreparedStatement ps = getConnection().prepareStatement(SEARCH_PRODUCTS_QUERY)){
            ps.setString(1, searchWildcard);
            ps.setString(2, searchWildcard);
            ps.setString(3, searchWildcard);
            ResultSet rs = ps.executeQuery();
            productList = parseProducts(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productList;
    }

    public JSONObject getAllProducts(){
        final String GET_PRODUCTS_QUERY = "SELECT * FROM products";
        JSONObject productList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_PRODUCTS_QUERY)){
            ResultSet rs = ps.executeQuery();
            productList = parseProducts(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productList;
    }

    public JSONObject getProductBySKU(String SKU){
        final String GET_PRODUCT_QUERY = "SELECT * FROM products WHERE SKU=?";
        //String details = null;
        JSONObject productList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_PRODUCT_QUERY)){
            ps.setString(1, SKU);
            ResultSet rs = ps.executeQuery();
            productList = parseProducts(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productList;
    }

    public JSONObject getProductById(int id){
        final String GET_PRODUCT_QUERY = "SELECT * FROM products WHERE id=?";
        //String details = null;
        JSONObject productList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_PRODUCT_QUERY)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            productList = parseProducts(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productList;
    }

    private JSONObject parseProducts(ResultSet rs) throws SQLException {
        JSONObject productList = new JSONObject();
        while (rs.next()) {
            Product newProduct = new Product();
            newProduct.setId(rs.getString(1));
            newProduct.setSKU(rs.getString(2));
            newProduct.setName(rs.getString(3));
            newProduct.setRRP(rs.getDouble(4));
            newProduct.setCost(rs.getDouble(5));
            newProduct.setDepartment(rs.getString(6));
            newProduct.setBrand(rs.getString(7));
            newProduct.setDescription(rs.getString(8));
            productList.append("products" , newProduct.toJson());
        }
        return productList;
    }

    public boolean existingSku(String sku) throws SQLException {
        final String GET_SKU_QUERY = "SELECT * FROM products WHERE SKU=?";
        try (PreparedStatement ps = getConnection().prepareStatement(GET_SKU_QUERY)) {
            ps.setString(1, sku);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public boolean deleteProductById(int id) {
        final String DELETE_PRODUCT_QUERY = "DELETE FROM products WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_PRODUCT_QUERY)) {
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
