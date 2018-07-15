package TEmPoS.db;

import TEmPoS.Model.Brand;
import TEmPoS.Model.Product;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Brands extends H2Base {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Customer.class);


    /**
     * establish connection to database
     * @param connectionSupplier     */
    public H2Brands(ConnectionSupplier connectionSupplier) {
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

    public boolean createBrand(Brand brand) throws SQLException {
        String query = "INSERT into brands (brand, distributor) VALUES(?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, brand.getBrand());
            ps.setString(2, brand.getDistributor());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editBrand(Brand brand) throws SQLException {
        String query = "UPDATE brands SET brand =?, distributor =? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, brand.getBrand());
            ps.setString(2, brand.getDistributor());
            ps.setInt(3, Integer.parseInt(brand.getId()));
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getBrands(){
        final String GET_BRANDS_QUERY = "SELECT * FROM brands";
        JSONObject brandList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_BRANDS_QUERY)){
            ResultSet rs = ps.executeQuery();
            brandList = parseBrands(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return brandList;
    }

    public boolean deleteBrand(int id){
        final String DELETE_BRAND_QUERY = "DELETE FROM brands WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_BRAND_QUERY)) {
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existingBrand(String brand) throws SQLException{
        final String GET_BRAND_QUERY = "SELECT * FROM brands WHERE brand=?";
        try (PreparedStatement ps = getConnection().prepareStatement(GET_BRAND_QUERY)) {
            ps.setString(1, brand);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public boolean deleteTable(){
        try (PreparedStatement ps = getConnection().prepareStatement("DROP TABLE brands")){
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject parseBrands(ResultSet rs) throws SQLException {
        JSONObject brandList = new JSONObject();
        while (rs.next()) {
            Brand newBrand = new Brand();
            newBrand.setId(rs.getString(1));
            newBrand.setBrand(rs.getString(2));
            newBrand.setDistributor(rs.getString(3));
            brandList.put(newBrand.getId() , newBrand.toJson());
        }
        return brandList;
    }
}
