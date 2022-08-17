package com.wabitts;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

/**
 * Hello world!
 *
 */
public class App {
    private static final String RPC_QUEUE_NAME = "rpc_queue";
    public static void main( String[] args ) throws Exception{
        try (RPCClient fibonacciRpc = new RPCClient()) {

            String i_str = Integer.toString(10);
            System.out.println(" [x] Requesting fib(" + i_str + ")");
            String response = fibonacciRpc.call(i_str);
            System.out.println(" [.] Got '" + response + "'");
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
