package com.wabitts;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

/**
 * Hello world!
 *
 */
public class App {
    private final static String EXCHANGE_NAME = "EXCH";
    public static void main( String[] args ) throws IOException, TimeoutException{
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.basicPublish(EXCHANGE_NAME, "", null, args[0].getBytes());
            System.out.println(" [x] Sent '" + args[0] + "'");
        }
    }
}
