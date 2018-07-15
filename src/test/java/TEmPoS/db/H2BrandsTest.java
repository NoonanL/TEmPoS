package TEmPoS.db;

import TEmPoS.Model.Brand;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;

import static org.junit.Assert.*;

public class H2BrandsTest {

    static final Logger LOG = LoggerFactory.getLogger(H2CustomerTest.class);

    private H2Brands db;
    private Brand test1 = new Brand("1","Vic Firth", "Korg");
    private Brand test2 = new Brand("Remo", "EMD");
    private Brand test3 = new Brand("Sabian", "Westside");

    @Before
    public void setUp() {
        db = new H2Brands(new ConnectionSupplier(ConnectionSupplier.FILE));
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
    public void createBrand() throws SQLException {
        System.out.println("=====================================");
        System.out.println("Creating Brands");
        System.out.println("=====================================");
        if(db.createBrand(test1) &&
                db.createBrand(test2) &&
                db.createBrand(test3)
                ){
            System.out.println("Brands successfuly added");
        }else{
            System.out.println("Brand creation failed");
        }
    }


    @Test
    public void editBrand() throws SQLException {
        System.out.println("=====================================");
        System.out.println("Testing brand editing");
        System.out.println("=====================================");
        System.out.println(test1.toString());
        test1.setDistributor("ha ha ha not EMD anymo' Yo");
        if(db.editBrand(test1)){
            System.out.println("Test product successfully edited.");
            System.out.println(db.getBrands());
        }else{
            System.out.println("Failed to create test product.");
        }
    }

    @Test
    public void getBrands() {
        System.out.println("=====================================");
        System.out.println("Getting Brands");
        System.out.println("=====================================");
        System.out.println(db.getBrands().toString());
    }

    @Test
    public void deleteBrand() {
        System.out.println("=====================================");
        System.out.println("Testing delete product by id");
        System.out.println("=====================================");
        if(db.deleteBrand(2)){
            System.out.println("Brand successfully deleted");
        }else{
            System.out.println("Failed to delete Brand");
        }
    }

    @Test
    public void existingBrand() throws SQLException {
        System.out.println("=====================================");
        System.out.println("Testing if brand already exists");
        System.out.println("=====================================");
        if(db.existingBrand("Vic Firth")){
            System.out.println("Brand already exists!!");
        }else{
            System.out.println("brand is unique.");
        }
    }
}