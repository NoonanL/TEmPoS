package TEmPoS.db;

import TEmPoS.Model.Product;
import TEmPoS.Model.PurchaseOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class H2PurchaseOrderTest {

    static final Logger LOG = LoggerFactory.getLogger(H2CustomerTest.class);

    private H2PurchaseOrder db;
    PurchaseOrder testPO = new PurchaseOrder();


    @Before
    public void setUp() throws Exception {
        db = new H2PurchaseOrder(new ConnectionSupplier(ConnectionSupplier.FILE));

        Map<String, String> productsMap = new HashMap<>();
        productsMap.put("123", "5");
        productsMap.put("456", "1");


        testPO.setUID("123");
        testPO.setStatus("Pending");
        testPO.setBranchId("Test Branch");
        testPO.setProducts(productsMap);
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
    public void createPurchaseOrder() throws SQLException {
 //       System.out.println(testPO.toString());
        System.out.println("=====================================");
        System.out.println("Testing Purchase Order creation");
        System.out.println("=====================================");
        if(db.createPurchaseOrder(testPO)){
            System.out.println("Test product successfully created.");
        }else{
            System.out.println("Failed to create test product.");
        }
    }

    @Test
    public void getPurchaseOrders() {
        System.out.println("=====================================");
        System.out.println("Testing get all purchase Orders");
        System.out.println("=====================================");
        System.out.println(db.getPurchaseOrders().toString());
    }

    @Test
    public void getPurchaseOrderByUID() {
        System.out.println("=====================================");
        System.out.println("Testing get purchase Order by Id");
        System.out.println("=====================================");
        System.out.println(db.getPurchaseOrderByUID(123).toString());

    }

    @Test
    public void editPurchaseOrder() {
    }

    @Test
    public void existingOrder() {
    }


}