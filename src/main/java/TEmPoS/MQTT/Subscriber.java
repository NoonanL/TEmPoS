package TEmPoS.MQTT;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Subscriber {

    public Subscriber() throws MqttException {
        //String username = "admin";
        //String password = "LiamNoonan";


        System.out.println("== START SUBSCRIBER ==");

        MqttClient client = new MqttClient("tcp://192.168.1.192:1883", "TEmPoS_Server");

        client.setCallback(new SimpleMqttCallBack());

        ///Set connection options (authentication, etc)
        MqttConnectOptions connOpt = new MqttConnectOptions();
        //connOpt.setUserName(username);
        //connOpt.setPassword(password.toCharArray());

        //Connect using ConnectionOptions
        client.connect(connOpt);

        //Test subscribe to topic
        client.subscribe("Test");
    }

    }

