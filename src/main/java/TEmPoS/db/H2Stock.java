package TEmPoS.db;

import TEmPoS.Model.Product;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Stock extends H2Base {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Customer.class);

    H2Stock(Connection connection) {
        super(connection);
    }

    /**
     * establish connection to database
     * @param connectionSupplier     */
    public H2Stock(ConnectionSupplier connectionSupplier) {
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

    public boolean createStockListing(int productId){
        String query = "INSERT into stock (productId, branch01, branch02) VALUES(?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editStockListing(int productId, String branchId, int newVal){
        final String EDIT_STOCK_QUERY = "UPDATE stock SET " + branchId + " =? WHERE productId=?";
        try (PreparedStatement ps = getConnection().prepareStatement(EDIT_STOCK_QUERY)) {
            ps.setInt(1, newVal);
            ps.setInt(2, productId);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getStockLevel(int productId, String branchId){
        final String SEARCH_STOCK_QUERY = "SELECT " + branchId + " FROM stock WHERE productId=? ";
        int stockLevel = 0;
        try (PreparedStatement ps = getConnection().prepareStatement(SEARCH_STOCK_QUERY)){
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                stockLevel = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stockLevel;
    }

    public void getBranchStock(String branchId){
        final String GET_BRANCH_QUERY = "SELECT * FROM stock";
        try (PreparedStatement ps = getConnection().prepareStatement(GET_BRANCH_QUERY)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt(2));
                System.out.println(rs.getInt(3));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean incrementStock(int productId, String branchId){
        final String INCREMENT_STOCK_QUERY = "UPDATE stock SET " + branchId + " = " + branchId + " + 1 WHERE productId=?";
        try (PreparedStatement ps = getConnection().prepareStatement(INCREMENT_STOCK_QUERY)) {
            ps.setInt(1, productId);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean decrementStock(int productId, String branchId){
        final String DECREMENT_STOCK_QUERY = "UPDATE stock SET " + branchId + " = " + branchId + " - 1 WHERE productId=?";
        try (PreparedStatement ps = getConnection().prepareStatement(DECREMENT_STOCK_QUERY)) {
            ps.setInt(1, productId);
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
        try (PreparedStatement ps = getConnection().prepareStatement("DROP TABLE stock")){
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
