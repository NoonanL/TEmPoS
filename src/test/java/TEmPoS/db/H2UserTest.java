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
        if(db.register("Test User For Testing Purposes", "password", "Y")){
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

        if(db.isRegistered("Test User For Testing Purposes")){
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

        if(db.login("Test User For Testing Purposes", "password")){
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
        System.out.println(db.getUserDetails("Test User For Testing Purposes"));
    }

    @Test
    public void editUserData(){
        System.out.println("=====================================");
        System.out.println("Testing edit user data (except password)");
        System.out.println("=====================================");
        if(db.register("Test User For Testing Purposes", "password", "Y")){
            System.out.println("User successfuly registered");
        }else{
            System.out.println("registration failed");
        }
        System.out.println(db.getUserDetails("Test User For Testing Purposes"));
        System.out.println("Editing...");
        if(db.editUser("Test User For Testing Purposes", "Test User For Testing Purposes Edited", "Y")){
            System.out.println("Test User successfully edited");
        }
        //returns test user to original state
        db.editUser("Test User Edited", "Test User For Testing Purposes", "Y");
    }

    @Test
    public void getUsers(){
        System.out.println(db.getUsers().toString());
    }

    @Test
    public void isAdmin(){
        System.out.println("=====================================");
        System.out.println("Testing if user has admin privileges.");
        System.out.println("=====================================");
        if(db.isAdmin("Test User For Testing Purposes")){
            System.out.println("User has admin status");
        }else{
            System.out.println("User does NOT have admin status");
        }
    }

    @Test
    public void deleteUser(){
        System.out.println("=====================================");
        System.out.println("Testing delete User.");
        System.out.println("=====================================");

        db.register("Delete me for Testing purposes", "password", "Y");
        System.out.println(db.getUsers().toString());
        db.deleteUser("Delete me for Testing purposes");
        System.out.println(db.getUsers().toString());

    }
}