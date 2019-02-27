package TEmPoS.db;

import TEmPoS.Model.Customer;
import TEmPoS.Model.Transaction;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Transactions extends H2Base {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Transactions.class);


    /**
     * establish connection to database
     * @param connectionSupplier     */
    public H2Transactions(ConnectionSupplier connectionSupplier) {
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


    public boolean createTransaction(Transaction transaction) {
        String query = "INSERT into transactions (customerId, customerName, productId, productName) VALUES (?, ?, ?, ?)";
        //System.out.println(transaction.getProductId());
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, transaction.getCustomerId());
            ps.setString(2, transaction.getCustomerName());
            ps.setString(3, transaction.getProductId());
            ps.setString(4, transaction.getProductName());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getTransactions(){
        final String GET_TRANSACTION_QUERY = "SELECT * FROM transactions";
        JSONObject transactionList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_TRANSACTION_QUERY)){
            ResultSet rs = ps.executeQuery();
            //System.out.println(rs);
            transactionList = parseTransactions(rs);
            //System.out.println(customerList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactionList;
    }


    private JSONObject parseTransactions(ResultSet rs) throws SQLException {
        JSONObject transactionList = new JSONObject();
        while (rs.next()) {
            Transaction newTransaction = new Transaction();
            newTransaction.setId(rs.getString(1));
            newTransaction.setCustomerId(rs.getString(2));
            newTransaction.setCustomerName(rs.getString(3));
            newTransaction.setProductId(rs.getString(4));
            newTransaction.setProductName(rs.getString(5));
           System.out.println("transaction: " + newTransaction.getCustomerId() + " , " + newTransaction.getProductId() + " , " + newTransaction.getCustomerName() + " , " + newTransaction.getProductName() );

            transactionList.append("transactions" , newTransaction.toJson());
        }
        return transactionList;
    }


    /**
     * BE CAREFUL WITH ME
     * Function to delete the customer table
     */
    public boolean deleteTable(){
        try (PreparedStatement ps = getConnection().prepareStatement("DROP TABLE transactions")){
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
