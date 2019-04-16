package TEmPoS.MQTT;

import org.eclipse.paho.client.mqttv3.*;

public class Publisher {

   private MqttClient client;

    public Publisher() throws MqttException {
//
//       this.client = new MqttClient("tcp://192.168.1.192:1883", "I am a test client");
//       client.connect();


    }

    public void publish(String topic, String messageString) throws MqttException {
      MqttMessage message = new MqttMessage();
      message.setPayload(messageString.getBytes());
      message.setRetained(true);
      client.publish(topic,message);
      client.disconnect();

    }


    public void disconnect() throws MqttException {
        client.disconnect();
    }

}
