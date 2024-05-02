package pl.com.pszerszenowicz.SWAPVerificationApi.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "fromApiGateway";
    public static final String RECEIVING_QUEUE = "sentFromApiGateway";
    public static final String SENDING_QUEUE = "receivedByApiGateway";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        return connectionFactory;
    }

    @Bean
    Queue sendingQueue() {
        return new Queue(SENDING_QUEUE);
    }

    @Bean
    Queue receivingQueue() {
        return new Queue(RECEIVING_QUEUE);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding sendBinding() {
        return BindingBuilder.bind(sendingQueue()).to(exchange()).with(SENDING_QUEUE);
    }

    @Bean
    Binding receiveBinding() {
        return BindingBuilder.bind(receivingQueue()).to(exchange()).with(RECEIVING_QUEUE);
    }

    @Bean
    RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

}
