package TEmPoS.MQTT;


import TEmPoS.db.H2Transactions;
import TEmPoS.db.H2User;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Subscriber {

    public Subscriber(String topic, String clientId, String type, H2Transactions h2Transactions, H2User h2User, Publisher publisher) throws MqttException {

        System.out.println("== START SUBSCRIBER - TOPIC : " + topic + " ==");

        MqttClient client = new MqttClient("tcp://192.168.1.192:1883", clientId);



        ///Set connection options (authentication, etc)
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setAutomaticReconnect(true);
        String test = clientId + " has disconnected unexpectedly!";
        connOpt.setWill("Debug", test.getBytes(), 2,true);
        connOpt.setCleanSession(true);
        //connOpt.setUserName(username);
        //connOpt.setPassword(password.toCharArray());

        //Connect using ConnectionOptions
        client.connect(connOpt);
        client.setCallback(new MqttTransactionCallback(h2Transactions,h2User, publisher));

        //Test subscribe to topic including QoS
        client.subscribe(topic,2);


    }
    public Subscriber(String topic, String clientId, String type) throws MqttException{

        System.out.println("== START SUBSCRIBER - TOPIC : " + topic + " ==");

        MqttClient client = new MqttClient("tcp://192.168.1.192:1883", clientId);



        ///Set connection options (authentication, etc)
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setAutomaticReconnect(true);
        String test = clientId + " has disconnected unexpectedly!";
        connOpt.setWill("Debug", test.getBytes(), 2,true);
        connOpt.setCleanSession(true);
        //connOpt.setUserName(username);
        //connOpt.setPassword(password.toCharArray());

        //Connect using ConnectionOptions
        client.connect(connOpt);
        client.setCallback(new SimpleMqttCallBack());



        //Test subscribe to topic including QoS
        client.subscribe(topic,2);
    }

    }

