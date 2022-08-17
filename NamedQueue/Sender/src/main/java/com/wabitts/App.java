package com.wabitts;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

/**
 * Hello world!
 *
 */
public class App 
{private final static String QUEUE_NAME = "hello";
    public static void main( String[] args ) throws IOException, TimeoutException{
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello Worldsss!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
