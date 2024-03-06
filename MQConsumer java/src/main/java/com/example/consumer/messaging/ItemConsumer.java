package com.example.consumer.messaging;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.example.consumer.models.Item;

public class ItemConsumer implements AutoCloseable {
    private String baseUrl;

    private ConnectionFactory connectionFactory;

    private Connection connection;

    private Session session;

    private Queue queue;

    private MessageConsumer consumer;

    private boolean inError;

    private String error;

    private static ItemConsumer instance;

    private ItemConsumer(String brokeUrl, String queueName) {
        baseUrl = brokeUrl;
        connectionFactory = new ActiveMQConnectionFactory();
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
            consumer = session.createConsumer(queue);
            consumer.setMessageListener(message -> {
                try {
                    int itemId = message.getIntProperty("id");
                    String itemTitle = message.getStringProperty("title");
                    Item i = new Item(itemId, itemTitle);
                    System.out.println(i.id + " " + i.title);
                } catch (Exception e) {
                    System.out.println(e);
                    return;
                }
            });
        } catch (Exception e) {
            inError = true;
            error = e.getMessage();
            return;
        }
    }

    @Override
    public void close() {
        try {
            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void initialize(String brokeUrl, String queueName) {
        instance = new ItemConsumer(brokeUrl, queueName);
    }

    public static String getError() {
        return instance.error;
    }

    public static void terminate() {
        instance.close();
    }
}
