package TEmPoS.db;

import TEmPoS.Model.GoodsOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class H2GoodsOrderTest {

    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(H2UserTest.class);

    private H2GoodsOrder db;

    private GoodsOrder testOrder;

    @Before
    public void setUp() {
        db = new H2GoodsOrder(new ConnectionSupplier(ConnectionSupplier.FILE));
        testOrder = new GoodsOrder();
        testOrder.setId("1");
        testOrder.setUID("f0d007ea-7ec3-4c46-94dd-2d851ed25a18");
        testOrder.setProductId("1");
        testOrder.setStatus("Pending");
        testOrder.setQuantity("1");
    }

    @After
    public void tearDown() {
        try {
            db.close();
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void createGoodsOrder() throws SQLException {

        System.out.println("=====================================");
        System.out.println("Testing Create Goods Order");
        System.out.println("=====================================");
        if(db.createGoodsOrder(testOrder)){
            System.out.println("Test Order successfully created");
        }else{
            System.out.println("registration failed");
        }

    }

    @Test
    public void editGoodsOrder() {
        //testOrder.setQuantity("1");
        //testOrder.setStatus("Complete");
        System.out.println("=====================================");
        System.out.println("Testing goods order editing");
        System.out.println("=====================================");
        if(db.editGoodsOrder(testOrder)){
            System.out.println("Test order successfully edited.");
            System.out.println(db.getGoodsOrderByUid(testOrder.getUID()));
        }else{
            System.out.println("Failed to edit Goods Order.");
        }
        System.out.println(testOrder.toJson());
    }

    @Test
    public void getGoodsOrderByUid() {
        System.out.println("=====================================");
        System.out.println("Testing get GoodsOrder by UID");
        System.out.println("=====================================");
        System.out.println(db.getGoodsOrderByUid("123"));
    }

    @Test
    public void getGoodsOrders() {
        System.out.println("=====================================");
        System.out.println("Testing get all goods Order");
        System.out.println("=====================================");
        System.out.println(db.getGoodsOrders());



    }
}