package com.heroku.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Test {

	public static void main(String[] args) throws Exception {
		String uri = System.getenv("CLOUDAMQP_URL");
		if (uri == null) uri = "amqps://bqctosod:I4imL3WG7FqlkXrbLBk0ydyXzVFypZnk@whale.rmq.cloudamqp.com/bqctosod";

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(uri);
		factory.setRequestedHeartbeat(30);
		factory.setConnectionTimeout(30);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare("hello", false, false, false, null);
		String message = "Hello CloudAMQP!!!";
		channel.basicPublish("", "hello", null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume("hello", true, consumer);
	}
}