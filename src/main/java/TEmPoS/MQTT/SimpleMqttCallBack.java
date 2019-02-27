package TEmPoS.MQTT;

import org.eclipse.paho.client.mqttv3.*;

public class SimpleMqttCallBack implements MqttCallback {


    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");

    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
       // System.out.println("Message received:\t"+ new String(mqttMessage.getPayload()) );

        //get json from message
        //parse json into key value pairs
        // process transaction

    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

}