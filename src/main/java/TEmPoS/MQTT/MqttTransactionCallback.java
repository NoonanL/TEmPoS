package TEmPoS.MQTT;

import TEmPoS.Model.Product;
import TEmPoS.Model.Transaction;
import TEmPoS.db.H2Products;
import TEmPoS.db.H2Transactions;
import TEmPoS.db.H2User;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

public class MqttTransactionCallback implements MqttCallback {

    private H2Transactions h2Transactions;
    private H2User h2User;
    private H2Products h2Products;
    private Publisher publisher;

    MqttTransactionCallback(H2Transactions h2Transactions, H2User h2User, H2Products h2Products, Publisher publisher){
        this.h2Transactions = h2Transactions;
        this.h2User = h2User;
        this.publisher = publisher;
        this.h2Products = h2Products;

    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");

    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws MqttException, SQLException {

        //get json from message
       String request = new String(mqttMessage.getPayload());
        //System.out.println("Message received:\t"+ new String(mqttMessage.getPayload()) );
//
//        //parse json into key value pairs

            JSONObject input = new JSONObject(request);
           // System.out.println(input);


//
//        //Get request user
        String requestUser = input.getString("requestUser");
//
//        //if user is valid
        if (h2User.isRegistered(requestUser)) {
//
//            //attempt process transaction

            JSONArray purchased = input.getJSONArray("products");

           for (int i = 0; i < purchased.length(); i++){
               //System.out.println(purchased.getJSONObject(i).toString());
               Transaction newTransaction = new Transaction();
               newTransaction.setCustomerId(input.getString("customerId"));
               newTransaction.setCustomerName(input.getString("customerName"));
               JSONObject item = purchased.getJSONObject(i);
               Product product = h2Products.returnProductById(Integer.parseInt(item.getString("productId")));

               newTransaction.setProductId(item.getString("productId"));
               newTransaction.setProductName(product.getName());
               newTransaction.setQuantity(item.getString("quantity"));

               if (h2Transactions.createTransaction(newTransaction)) {
                   System.out.println("Transaction Message recieved and processed!");
//
               }
           }
            //get vars
            //try create transaction
//            newTransaction.setProductId("Test");
//            newTransaction.setProductName("Test");
//
//            //if process transaction returns true:
//            if (h2Transactions.createTransaction(newTransaction)) {
//                System.out.println("Transaction Message recieved and processed!");
//
//                publisher.publish("update", "I am a message");
//
////
////
//           }
       }
    }

    //Override method for on delivery - unnused here.
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

}