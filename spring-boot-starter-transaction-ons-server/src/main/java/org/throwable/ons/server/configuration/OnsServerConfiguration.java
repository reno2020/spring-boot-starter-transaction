package org.throwable.ons.server.configuration;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/24 14:16
 */
@ConditionalOnClass(value = {RabbitTemplate.class, CachingConnectionFactory.class})
@EnableConfigurationProperties(value = {RabbitProperties.class,OnsServerProperties.class})
@Configuration
public class OnsServerConfiguration {

	private final RabbitProperties rabbitProperties;
	private final OnsServerProperties onsServerProperties;

	public OnsServerConfiguration(RabbitProperties rabbitProperties, OnsServerProperties onsServerProperties) {
		this.rabbitProperties = rabbitProperties;
		this.onsServerProperties = onsServerProperties;
	}
}
