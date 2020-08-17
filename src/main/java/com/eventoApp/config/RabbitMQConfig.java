package com.eventoApp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@Configuration
public class RabbitMQConfig {
	
	   private Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);


		@Bean
		public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
																				   ConnectionFactory connectionFactory) {
			SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
			configurer.configure(factory, connectionFactory);
			factory.setConcurrentConsumers(1);
			factory.setMessageConverter(new SimpleMessageConverter());
			factory.setErrorHandler(rabbitErrorHandler());
			return factory;
		}
		
		@Bean
		public MessageConverter jsonMessageConverter() {
			return new Jackson2JsonMessageConverter();
		}

		@Bean
		public AmqpTemplate template(ConnectionFactory connectionFactory) {
			RabbitTemplate template = new RabbitTemplate(connectionFactory);
			template.setMessageConverter(jsonMessageConverter());
			template.setChannelTransacted(true);
			return template;
		}

		public ErrorHandler rabbitErrorHandler() {
			log.info("RabbitMQConfig:rabbitErrorHandler() - Error when configure RabbitMQ sender bean!");
			return null;
		}
}
