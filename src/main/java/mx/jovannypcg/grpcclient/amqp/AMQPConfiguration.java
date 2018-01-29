package mx.jovannypcg.grpcclient.amqp;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfiguration {
    private static final String QUEUE_NAME = "repository_enrollments";

    @Value("${spring.rabbitmq.host}")     private String host;
    @Value("${spring.rabbitmq.username}") private String username;
    @Value("${spring.rabbitmq.password}") private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);

        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        //The routing key is set to the name of the queue by the broker for the default exchange.
        template.setRoutingKey(QUEUE_NAME);
        //Where we will synchronously receive messages from
        template.setQueue(QUEUE_NAME);
        return template;
    }

    @Bean
    // Every queue is bound to the default direct exchange
    public Queue helloWorldQueue() {
        return new Queue(QUEUE_NAME);
    }
}
