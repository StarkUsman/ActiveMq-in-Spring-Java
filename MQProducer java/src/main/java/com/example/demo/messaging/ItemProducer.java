package com.example.demo.messaging;

import javax.jms.*;

import com.example.demo.models.Item;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ItemProducer implements AutoCloseable {

    private String baseUrl;

    private ConnectionFactory connectionFactory;

    private Connection connection;

    private Session session;

    private Queue queue;

    private MessageProducer producer;

    private boolean inError;

    private String error;

    private static ItemProducer instance;

    private ItemProducer(String brokerUrl, String queueName) {
        baseUrl = brokerUrl;
        connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        try {
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (Exception e) {
            inError = true;
            error = e.getMessage();
            return;
        }
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (Exception e) {
            inError = true;
            error = e.getMessage();
            return;
        }
        try {
            queue = session.createQueue(queueName);
        } catch (Exception e) {
            inError = true;
            error = e.getMessage();
            return;
        }
        try {
            producer = session.createProducer(queue);
        } catch (Exception e) {
            inError = true;
            error = e.getMessage();
            return;
        }
        inError = false;

    }
    private void publish(Item item) {
        try {
            var msg = session.createObjectMessage(item.id);
            msg.setIntProperty("id", item.id);
            msg.setStringProperty("title", item.title);
            producer.send(msg);
        } catch (Exception e) {
            inError = true;
            error = e.getMessage();
            return;
        }
    }

    @Override
    public void close() {
        try {
            producer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            inError = true;
            error = e.getMessage();
            return;
        }
    }

    public static boolean inError() {
        return instance.inError;
    }

    public static String getError() {
        return instance.error;
    }

    public static void initialize(String brokerUrl, String queueName) {
        instance = new ItemProducer(brokerUrl, queueName);
    }

    public static void terminate() {
        instance.close();
    }

    public static void addItem(Item item) {
        instance.publish(item);
    }
}
