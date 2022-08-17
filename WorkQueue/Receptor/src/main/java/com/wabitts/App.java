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
public class App 
{private final static String QUEUE_NAME = "hello2";
    public static void main( String[] args ) throws IOException, TimeoutException{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        //Give one task to the unbusy worker
        channel.basicQos(1);


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
          
            System.out.println(" [x] Received '" + message + "'");
            try {
              doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
              System.out.println(" [x] Done");
              channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
          };
          boolean autoAck = false; // acknowledgment is covered below
          channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });
    }

    private static void doWork(String message) throws InterruptedException {
        for (char ch: message.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
