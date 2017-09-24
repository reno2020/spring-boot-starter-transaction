package org.throwable.ons.client.configuration;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.throwable.ons.client.listener.FireTransactionListener;
import org.throwable.ons.client.listener.TransactionCheckerListener;
import org.throwable.ons.client.support.TransactionMessageTemplate;
import org.throwable.ons.client.support.TransactionThreadPoolProvider;
import org.throwable.ons.core.common.constants.Constants;
import org.throwable.ons.core.utils.NetUtils;
import org.throwable.protocol.constants.ListenerEnum;
import org.throwable.protocol.constants.QueueEnum;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/22 15:59
 */
@Configuration
@ConditionalOnClass(value = {RabbitTemplate.class, CachingConnectionFactory.class})
@EnableConfigurationProperties(value = {OnsClientProperties.class, RabbitProperties.class})
public class OnsClientConfiguration implements SmartInitializingSingleton, EnvironmentAware, BeanFactoryAware {

	private static final String HALF_MESSAGE_QUEUE = QueueEnum.HALF_MESSAGE_QUEUE.getValue();
	private static final String FIRE_TRANSACTION_QUEUE = QueueEnum.FIRE_TRANSACTION_QUEUE.getValue();
	private static final String CHECKER_QUEUE = QueueEnum.CHECKER_QUEUE.getValue();
	private final OnsClientProperties onsClientProperties;
	private final RabbitProperties rabbitProperties;
	private Integer serverPort;
	private DefaultListableBeanFactory beanFactory;

	@Autowired(required = false)
	private ApplicationEventPublisher applicationEventPublisher;

	public OnsClientConfiguration(OnsClientProperties onsClientProperties, RabbitProperties rabbitProperties) {
		this.onsClientProperties = onsClientProperties;
		this.rabbitProperties = rabbitProperties;
	}

	@Override
	public void setEnvironment(Environment environment) {
		serverPort = environment.getProperty("server.port", Integer.class);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}

	@ConditionalOnMissingBean(value = TransactionThreadPoolProvider.class)
	@Bean(name = "transactionThreadPoolProvider")
	public TransactionThreadPoolProvider transactionThreadPoolProvider() {
		return new TransactionThreadPoolProvider(
				onsClientProperties.getTransactionThreadPoolCoreSize(),
				onsClientProperties.getTransactionThreadPoolMaxSize(),
				onsClientProperties.getTransactionThreadPoolQueueCapacity(),
				onsClientProperties.getTransactionThreadPoolKeepAliveSeconds());
	}

	@Bean
	@ConditionalOnClass(ConnectionFactory.class)
	public CachingConnectionFactory rabbitConnectionFactory() throws Exception {
		RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
		if (rabbitProperties.determineHost() != null) {
			factory.setHost(rabbitProperties.determineHost());
		}
		factory.setPort(rabbitProperties.determinePort());
		if (rabbitProperties.determineUsername() != null) {
			factory.setUsername(rabbitProperties.determineUsername());
		}
		if (rabbitProperties.determinePassword() != null) {
			factory.setPassword(rabbitProperties.determinePassword());
		}
		if (rabbitProperties.determineVirtualHost() != null) {
			factory.setVirtualHost(rabbitProperties.determineVirtualHost());
		}
		if (rabbitProperties.getRequestedHeartbeat() != null) {
			factory.setRequestedHeartbeat(rabbitProperties.getRequestedHeartbeat());
		}
		RabbitProperties.Ssl ssl = rabbitProperties.getSsl();
		if (ssl.isEnabled()) {
			factory.setUseSSL(true);
			if (ssl.getAlgorithm() != null) {
				factory.setSslAlgorithm(ssl.getAlgorithm());
			}
			factory.setKeyStore(ssl.getKeyStore());
			factory.setKeyStorePassphrase(ssl.getKeyStorePassword());
			factory.setTrustStore(ssl.getTrustStore());
			factory.setTrustStorePassphrase(ssl.getTrustStorePassword());
		}
		if (rabbitProperties.getConnectionTimeout() != null) {
			factory.setConnectionTimeout(rabbitProperties.getConnectionTimeout());
		}
		factory.afterPropertiesSet();
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
				factory.getObject());
		connectionFactory.setAddresses(rabbitProperties.determineAddresses());
		connectionFactory.setPublisherConfirms(rabbitProperties.isPublisherConfirms());
		connectionFactory.setPublisherReturns(rabbitProperties.isPublisherReturns());
		if (rabbitProperties.getCache().getChannel().getSize() != null) {
			connectionFactory
					.setChannelCacheSize(rabbitProperties.getCache().getChannel().getSize());
		}
		if (rabbitProperties.getCache().getConnection().getMode() != null) {
			connectionFactory
					.setCacheMode(rabbitProperties.getCache().getConnection().getMode());
		}
		if (rabbitProperties.getCache().getConnection().getSize() != null) {
			connectionFactory.setConnectionCacheSize(
					rabbitProperties.getCache().getConnection().getSize());
		}
		if (rabbitProperties.getCache().getChannel().getCheckoutTimeout() != null) {
			connectionFactory.setChannelCheckoutTimeout(
					rabbitProperties.getCache().getChannel().getCheckoutTimeout());
		}
		connectionFactory.setPublisherConfirms(true);  //force use publisher confirms
		return connectionFactory;
	}

	@ConditionalOnMissingBean(name = "clientTransactionRabbitTemplate")
	@Bean(name = "clientTransactionRabbitTemplate")
	public RabbitTemplate rabbitTemplate(CachingConnectionFactory rabbitConnectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(rabbitConnectionFactory);
		rabbitTemplate.setEncoding(Constants.ENCODING);
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		return rabbitTemplate;
	}

	@ConditionalOnMissingBean(name = "clientTransactionRabbitAdmin")
	@Bean(name = "clientTransactionRabbitAdmin")
	public RabbitAdmin rabbitAdmin(CachingConnectionFactory rabbitConnectionFactory) {
		return new RabbitAdmin(rabbitConnectionFactory);
	}

	@ConditionalOnMissingBean(value = TransactionMessageTemplate.class)
	@Bean(name = "transactionMessageTemplate")
	public TransactionMessageTemplate transactionMessageTemplate(TransactionThreadPoolProvider transactionThreadPoolProvider,
																 RabbitTemplate clientTransactionRabbitTemplate) {
		TransactionMessageTemplate transactionMessageTemplate = new TransactionMessageTemplate();
		transactionMessageTemplate.setRabbitTemplate(clientTransactionRabbitTemplate);
		transactionMessageTemplate.setThreadPoolProvider(transactionThreadPoolProvider);
		return transactionMessageTemplate;
	}

	@Override
	public void afterSingletonsInstantiated() {
		//fetch host and port
		String suffix;
		if (StringUtils.hasText(onsClientProperties.getTopicExchangeSuffix())) {
			suffix = onsClientProperties.getTopicExchangeSuffix();
		} else {
			InetAddress inetAddress = NetUtils.getLocalHostLanAddress();
			String hostString = null == inetAddress ? null : inetAddress.getHostAddress();
			String portString = null;
			try {
				portString = NetUtils.getServerPort(false);
				if (!StringUtils.hasText(portString)) {
					portString = NetUtils.getServerPort(true);
				}
			} catch (Exception e) {
				//ignore
			}
			Assert.isTrue(StringUtils.hasText(hostString), "Fetch application server host failed!");
			if (null != serverPort) {
				suffix = "." + hostString + ":" + serverPort;
			} else {
				Assert.isTrue(StringUtils.hasText(portString), "Fetch application server port failed!");
				suffix = "." + NetUtils.getLocalHostLanAddress() + ":" + portString;
			}
			Assert.isTrue(StringUtils.hasText(suffix), "Create [exchange]routingKey suffix for [TransactionMessageTemplate] failed!");
		}
		//process rabbitmq components
		processRabbitmqComponents(suffix);
	}

	private void processRabbitmqComponents(String suffix) {
		List<String> queuesToDeclare = new ArrayList<>(3);
		String halfMessageRoutingKey = HALF_MESSAGE_QUEUE + suffix;
		String fireTransactionRoutingKey = FIRE_TRANSACTION_QUEUE + suffix;
		String checkerRoutingKey = CHECKER_QUEUE + suffix;
		queuesToDeclare.add(halfMessageRoutingKey);
		queuesToDeclare.add(fireTransactionRoutingKey);
		queuesToDeclare.add(checkerRoutingKey);
		declareRabbitmqQueues(queuesToDeclare);
		processExchangeRoutingKeyForTransactionMessageTemplate(halfMessageRoutingKey, suffix);
		CachingConnectionFactory cachingConnectionFactory = beanFactory.getBean("rabbitConnectionFactory", CachingConnectionFactory.class);
		registerFireTransactionListenerBean(fireTransactionRoutingKey, cachingConnectionFactory);
		registerTransactionCheckListenerBean(checkerRoutingKey, cachingConnectionFactory);
	}

	private void declareRabbitmqQueues(List<String> queuesToDeclare) {
		RabbitAdmin transactionRabbitAdmin = beanFactory.getBean("clientTransactionRabbitAdmin", RabbitAdmin.class);
		for (String queueToDeclare : queuesToDeclare) {
			Queue queue = new Queue(queueToDeclare, true, false, false, null);
			transactionRabbitAdmin.declareQueue(queue);
			DirectExchange exchange = new DirectExchange(queueToDeclare, true, false);
			transactionRabbitAdmin.declareExchange(exchange);
			transactionRabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(queueToDeclare));
		}
	}

	private void processExchangeRoutingKeyForTransactionMessageTemplate(String halfMessageRoutingKey, String suffix) {
		TransactionMessageTemplate transactionMessageTemplate = beanFactory.getBean("transactionMessageTemplate", TransactionMessageTemplate.class);
		transactionMessageTemplate.setExchangeRoutingKey(halfMessageRoutingKey);
		transactionMessageTemplate.setExchangeRoutingKeySuffix(suffix);
	}

	private void registerFireTransactionListenerBean(String fireTransactionRoutingKey, CachingConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(fireTransactionRoutingKey);
		container.setAutoStartup(true);
		container.setConcurrentConsumers(onsClientProperties.getFireTransactionListenerConcurrentConsumers());
		container.setMaxConcurrentConsumers(onsClientProperties.getFireTransactionListenerMaxConcurrentConsumers());
		container.setupMessageListener(new FireTransactionListener());
		if (null != applicationEventPublisher) {
			container.setApplicationEventPublisher(applicationEventPublisher);
		}
		beanFactory.registerSingleton(ListenerEnum.FIRE_TRANSACTION_LISTENER.getValue(), container);
		beanFactory.getBean(ListenerEnum.FIRE_TRANSACTION_LISTENER.getValue(), SimpleMessageListenerContainer.class);
	}

	private void registerTransactionCheckListenerBean(String checkerRoutingKey, CachingConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(checkerRoutingKey);
		container.setAutoStartup(true);
		container.setConcurrentConsumers(onsClientProperties.getTransactionCheckerListenerConcurrentConsumers());
		container.setMaxConcurrentConsumers(onsClientProperties.getTransactionCheckerListenerMaxConcurrentConsumers());
		container.setupMessageListener(new TransactionCheckerListener());
		if (null != applicationEventPublisher) {
			container.setApplicationEventPublisher(applicationEventPublisher);
		}
		beanFactory.registerSingleton(ListenerEnum.TRANSACTION_CHECKER_LISTENER.getValue(), container);
		beanFactory.getBean(ListenerEnum.TRANSACTION_CHECKER_LISTENER.getValue(), SimpleMessageListenerContainer.class);
	}
}
