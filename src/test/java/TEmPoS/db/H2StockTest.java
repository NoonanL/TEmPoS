package TEmPoS.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class H2StockTest {

    static final Logger LOG = LoggerFactory.getLogger(H2CustomerTest.class);
    private H2Stock db;

    @Before
    public void setUp() {
        db = new H2Stock(new ConnectionSupplier(ConnectionSupplier.FILE));
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
    public void createStockListing() {
        System.out.println("=====================================");
        System.out.println("Testing stock row creation");
        System.out.println("=====================================");
        if(db.createStockListing(123)){
            System.out.println("Test stock listing successfully created.");
        }else{
            System.out.println("Failed to create test stock listing.");
        }

    }

    @Test
    public void editStockListing() {
        System.out.println("=====================================");
        System.out.println("Testing stock editing");
        System.out.println("=====================================");
        if(db.editStockListing(123, "branch01", 5)){
            System.out.println("Test stock listing successfully edited.");
            System.out.println(db.getStockLevel(123, "branch01"));
        }else{
            System.out.println("Failed to edit stock listing.");
        }
    }

    @Test
    public void getStockLevel() {
        System.out.println("=====================================");
        System.out.println("Testing get stock listing");
        System.out.println("=====================================");
        System.out.println(db.getStockLevel(4, "Branch01"));
    }

    @Test
    public void getAll() {
        System.out.println("=====================================");
        System.out.println("Testing get all stock");
        System.out.println("=====================================");
        db.getBranchStock("Branch01");
    }

    @Test
    public void incrementStock() {
        System.out.println("=====================================");
        System.out.println("Testing stock incrementing");
        System.out.println("=====================================");
        if(db.incrementStock(123, "branch01")){
            System.out.println("Test stock listing successfully incremented.");
            System.out.println(db.getStockLevel(123, "branch01"));
        }else{
            System.out.println("Failed to increment stock listing.");
        }
    }

    @Test
    public void decrementStock() {
        System.out.println("=====================================");
        System.out.println("Testing stock decrementing");
        System.out.println("=====================================");
        if(db.decrementStock(123, "branch01")){
            System.out.println("Test stock listing successfully decrement.");
            System.out.println(db.getStockLevel(123, "branch01"));
        }else{
            System.out.println("Failed to decrement stock listing.");
        }
    }
}