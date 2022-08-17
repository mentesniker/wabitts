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
    private final static String QUEUE_NAME = "hello2";
    public static void main( String[] args ) throws IOException, TimeoutException{
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            //send message to the default exchange, the default exchange redirects its content to the queu
            //with name routing key
            channel.basicPublish("", "hello2", MessageProperties.PERSISTENT_TEXT_PLAIN, args[0].getBytes());
            System.out.println(" [x] Sent '" + args[0] + "'");
        }
    }
}
