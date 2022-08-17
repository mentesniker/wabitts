package com.wabitts;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 *
 */
public class App {
  private final static String EXCH_NAME = "topic_logs";
    public static void main( String[] args ) throws IOException, TimeoutException{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCH_NAME, "topic");
        String queuName = channel.queueDeclare().getQueue();
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        //para hacer matchs de topics se puede usar * para hacer match 
        // de una palabra o # para hacer match de varias palabras
        //las palabras siempre van separadas por puntos .
        channel.queueBind(queuName, EXCH_NAME, "*.warning");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
          
            System.out.println(" [x] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
          };
          channel.basicConsume(queuName, true, deliverCallback, consumerTag -> { });
    }
}
