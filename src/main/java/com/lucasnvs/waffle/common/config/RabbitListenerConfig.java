package com.lucasnvs.waffle.common.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitListenerConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory
    ) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(2);

        factory.setDefaultRequeueRejected(false);

        factory.setRecoveryInterval(10000L); // 10s entre tentativas

        factory.setMissingQueuesFatal(false);

        return factory;
    }
}
