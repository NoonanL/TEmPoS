package TEmPoS.db;

import TEmPoS.Model.Customer;
import TEmPoS.Model.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class H2ProductsTest {

    static final Logger LOG = LoggerFactory.getLogger(H2CustomerTest.class);

    private H2Products db;
    Product testProduct = new Product();

    @Before
    public void setUp() {
        db = new H2Products(new ConnectionSupplier(ConnectionSupplier.FILE));
        testProduct.setSKU("12345");
        testProduct.setName("Vic Firth 5A");
        testProduct.setRRP(11.00);
        testProduct.setCost(5.65);
        testProduct.setDepartment("Sticks");
        testProduct.setBrand("Vic Firth");
        testProduct.setDescription("One pair of Vic Firth 5A Drumsticks.");
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
    public void createProduct() {
        System.out.println("=====================================");
        System.out.println("Testing product creation");
        System.out.println("=====================================");
        if(db.createProduct(testProduct)){
            System.out.println("Test product successfully created.");
        }else{
            System.out.println("Failed to create test product.");
        }
    }

    @Test
    public void editProduct() {
        System.out.println("=====================================");
        System.out.println("Testing product editing");
        System.out.println("=====================================");
        System.out.println(testProduct.toString());
        testProduct.setName("Vic 5A Edited Yo");
        if(db.editProduct(2, testProduct)){
            System.out.println("Test product successfully edited.");
            System.out.println(db.getProductBySKU("12345").toString());
        }else{
            System.out.println("Failed to create test product.");
        }
    }

    @Test
    public void searchProducts() {
        System.out.println("=====================================");
        System.out.println("Testing product search");
        System.out.println("=====================================");
        System.out.println(db.searchProducts("Vic"));
    }

    @Test
    public void getAllProducts() {
        System.out.println("=====================================");
        System.out.println("Testing get all products");
        System.out.println("=====================================");
        System.out.println(db.getAllProducts().toString());
    }

    @Test
    public void checkSku() throws SQLException {
        System.out.println("=====================================");
        System.out.println("Testing if SKU already exists");
        System.out.println("=====================================");
        if(db.existingSku("12345")){
            System.out.println("SKU already exists!!");
        }else{
            System.out.println("SKU is unique.");
        }
    }

    @Test
    public void deleteProductById() {
        System.out.println("=====================================");
        System.out.println("Testing delete product by id");
        System.out.println("=====================================");
        if(db.deleteProductById(1)){
            System.out.println("Product successfully deleted");
        }else{
            System.out.println("Failed to delete product");
        }

    }
}