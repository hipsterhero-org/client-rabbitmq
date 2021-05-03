package com.heroku.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Test {

	public static void main(String[] args) throws Exception {
		String uri = System.getenv("CLOUDAMQP_URL");
		if (uri == null) uri = "amqps://bqctosod:I4imL3WG7FqlkXrbLBk0ydyXzVFypZnk@whale.rmq.cloudamqp.com/bqctosod";

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(uri);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		String queue = "hello";     //queue name
		boolean durable = true;    //durable - RabbitMQ will never lose the queue if a crash occurs
		boolean exclusive = false;  //exclusive - if queue only will be used by one connection
		boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes

		channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
		String message = "Hello CloudAMQP!";

		String exchangeName = "";
		String routingKey = "hello";
		channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message1 = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + message1 + "'");
		};
		channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
	}
}
