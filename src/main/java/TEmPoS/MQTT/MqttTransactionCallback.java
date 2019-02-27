package TEmPoS.MQTT;

import TEmPoS.Model.Transaction;
import TEmPoS.db.H2Transactions;
import TEmPoS.db.H2User;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class MqttTransactionCallback implements MqttCallback {

    private H2Transactions h2Transactions;
    private H2User h2User;
    private Publisher publisher;

    MqttTransactionCallback(H2Transactions h2Transactions, H2User h2User, Publisher publisher){
        this.h2Transactions = h2Transactions;
        this.h2User = h2User;
        this.publisher = publisher;

    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");

    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws MqttException {

        //get json from message
        String request = new String(mqttMessage.getPayload());
        //System.out.println("Message received:\t"+ new String(mqttMessage.getPayload()) );

        //parse json into key value pairs
        JSONObject input = new JSONObject(request);

        //Get request user
        String requestUser = input.getString("requestUser");

        //if user is valid
        if (h2User.isRegistered(requestUser)) {

            //attempt process transaction
            Transaction newTransaction = new Transaction();
            newTransaction.setCustomerId(input.getString("customerId"));
            newTransaction.setCustomerName(input.getString("customerName"));
            newTransaction.setProductId(input.getString("productId"));
            newTransaction.setProductName(input.getString("productName"));

            //if process transaction returns true:
            if (h2Transactions.createTransaction(newTransaction)) {
                System.out.println("Transaction Message recieved and processed!");

                publisher.publish("update", "I am a message");



            }
        }
    }

    //Override method for on delivery - unnused here.
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

}