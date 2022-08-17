package com.wabitts;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RPCClient implements AutoCloseable{

    private Connection connection;

    private Channel channel;

    private String requestQueueName = "rpc_queue";

    public RPCClient() throws IOException, TimeoutException{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    @Override
    public void close() throws IOException {
        connection.close();
        
    }

    public String call(String i_str) throws IOException, InterruptedException{
        final String corrId = UUID.randomUUID().toString();

        String replyQueuName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueuName)
                .build();
        
        channel.basicPublish("", requestQueueName, props, i_str.getBytes());

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        String ctag = channel.basicConsume(replyQueuName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        String result = response.take();
        channel.basicCancel(ctag);

        return result;
    }
    
}
