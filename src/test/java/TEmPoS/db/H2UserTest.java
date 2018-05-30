package TEmPoS.db;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class H2UserTest {

    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(H2UserTest.class);

    private H2User db;


    @Before
    public void setUp() {
        db = new H2User(new ConnectionSupplier(ConnectionSupplier.FILE));
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
    public void register(){
        System.out.println("=====================================");
        System.out.println("Testing User Registration - will now fail because database with test user already exists");
        System.out.println("=====================================");
        if(db.register("Test User2", "password", "Y")){
            System.out.println("User successfuly registered");
        }else{
            System.out.println("registration failed");
        }
    }

    @Test
    public void isRegistered() {
        System.out.println("=====================================");
        System.out.println("Testing successful isRegistered");
        System.out.println("=====================================");

        if(db.isRegistered("Test User")){
            System.out.println("User is registered.");
        }else{
            System.out.println("User is NOT registered.");
        }

        System.out.println("=====================================");
        System.out.println("Testing failed isRegistered");
        System.out.println("=====================================");

        if(db.isRegistered("Not A Registered User")){
            System.out.println("User is registered.");
        }else{
            System.out.println("User is NOT registered.");
        }
    }

    @Test
    public void login() {

        System.out.println("=====================================");
        System.out.println("Testing Successful Login");
        System.out.println("=====================================");

        if(db.login("Test User", "password")){
            System.out.println("User successfully logged in");
            }else{
            System.out.println("Login Failed");
        }

        System.out.println("=====================================");
        System.out.println("Testing Failed Login");
        System.out.println("=====================================");

        if(db.login("Wrong Username", "Wrong Pass")){
            System.out.println("User successfully logged in");
        }else{
            System.out.println("Login Failed");
        }

        if(db.login("Test User", "Wrong Pass")){
            System.out.println("User successfully logged in");
        }else{
            System.out.println("Login Failed");
        }

        if(db.login("Wrong Username", "password")){
            System.out.println("User successfully logged in");
        }else{
            System.out.println("Login Failed");
        }
    }

    @Test
    public void getUserData(){
        System.out.println("=====================================");
        System.out.println("Testing get user data (except password)");
        System.out.println("=====================================");
        System.out.println(db.getUserDetails("Test User2"));
    }
    @Test
    public void getUsers(){
        JSONObject responseJson = new JSONObject(db.getUsers());
        System.out.println(responseJson);
    }

    @Test
    public void isAdmin(){
        System.out.println("=====================================");
        System.out.println("Testing if user has admin privileges.");
        System.out.println("=====================================");
        if(db.isAdmin("Test User")){
            System.out.println("User has admin status");
        }else{
            System.out.println("User does NOT have admin status");
        }
    }
}