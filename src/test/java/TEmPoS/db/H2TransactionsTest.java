package TEmPoS.db;

import TEmPoS.Model.Product;
import TEmPoS.Model.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class H2TransactionsTest {

    static final Logger LOG = LoggerFactory.getLogger(H2CustomerTest.class);
    private H2Transactions db;
    Transaction newTransaction = new Transaction();


    @Before
    public void setUp() throws Exception {
        this.db = new H2Transactions(new ConnectionSupplier(ConnectionSupplier.FILE));

        this.newTransaction.setCustomerId("12345");
        this.newTransaction.setCustomerName("TestName");
        this.newTransaction.setProductId("Test Product");
        this.newTransaction.setProductName("Test Product Name");

    }

    @After
    public void tearDown() throws Exception {
        try {
            db.close();
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void createTransaction() {

        System.out.println("=====================================");
        System.out.println("Testing transaction creation");
        System.out.println("=====================================");
        if (db.createTransaction(newTransaction)) {
            System.out.println("Test transaction successfully created.");
        } else {
            System.out.println("Failed to create test transaction.");
        }


    }

    @Test
    public void getTransactions() {

        System.out.println("=====================================");
        System.out.println("Testing get all transactions");
        System.out.println("=====================================");
        System.out.println(db.getTransactions().toString());


    }
}