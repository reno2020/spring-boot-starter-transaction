package org.throwable.ons.client.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/22 15:59
 */
@ConfigurationProperties(prefix = OnsClientProperties.PREFIX)
public class OnsClientProperties {

	public static final String PREFIX = "ons.transaction.client";

	private static final Integer DOUBLE_LOGIC_CPU_NUMBER = Runtime.getRuntime().availableProcessors() * 2;
	private static final Integer DEFAULT_QUEUE_CAPACITY = 100;
	private static final Long DEFAULT_KEEP_ALIVE_SECONDS = 0L;

	private static final Integer DEFAULT_CONCURRENT_CONSUMERS = 10;
	private static final Integer DEFAULT_MAX_CONCURRENT_CONSUMERS = 20;

	private String topicExchangeSuffix;

	private Integer transactionThreadPoolCoreSize = DOUBLE_LOGIC_CPU_NUMBER;
	private Integer transactionThreadPoolMaxSize = DOUBLE_LOGIC_CPU_NUMBER;
	private Integer transactionThreadPoolQueueCapacity = DEFAULT_QUEUE_CAPACITY;
	private Long transactionThreadPoolKeepAliveSeconds = DEFAULT_KEEP_ALIVE_SECONDS;

	private Integer fireTransactionListenerConcurrentConsumers = DEFAULT_CONCURRENT_CONSUMERS;
	private Integer fireTransactionListenerMaxConcurrentConsumers = DEFAULT_MAX_CONCURRENT_CONSUMERS;
	private Integer transactionCheckerListenerConcurrentConsumers = DEFAULT_CONCURRENT_CONSUMERS;
	private Integer transactionCheckerListenerMaxConcurrentConsumers = DEFAULT_MAX_CONCURRENT_CONSUMERS;

	public Integer getTransactionThreadPoolCoreSize() {
		return transactionThreadPoolCoreSize;
	}

	public void setTransactionThreadPoolCoreSize(Integer transactionThreadPoolCoreSize) {
		this.transactionThreadPoolCoreSize = transactionThreadPoolCoreSize;
	}

	public Integer getTransactionThreadPoolMaxSize() {
		return transactionThreadPoolMaxSize;
	}

	public void setTransactionThreadPoolMaxSize(Integer transactionThreadPoolMaxSize) {
		this.transactionThreadPoolMaxSize = transactionThreadPoolMaxSize;
	}

	public Integer getTransactionThreadPoolQueueCapacity() {
		return transactionThreadPoolQueueCapacity;
	}

	public void setTransactionThreadPoolQueueCapacity(Integer transactionThreadPoolQueueCapacity) {
		this.transactionThreadPoolQueueCapacity = transactionThreadPoolQueueCapacity;
	}

	public Long getTransactionThreadPoolKeepAliveSeconds() {
		return transactionThreadPoolKeepAliveSeconds;
	}

	public void setTransactionThreadPoolKeepAliveSeconds(Long transactionThreadPoolKeepAliveSeconds) {
		this.transactionThreadPoolKeepAliveSeconds = transactionThreadPoolKeepAliveSeconds;
	}

	public String getTopicExchangeSuffix() {
		return topicExchangeSuffix;
	}

	public void setTopicExchangeSuffix(String topicExchangeSuffix) {
		this.topicExchangeSuffix = topicExchangeSuffix;
	}

	public Integer getFireTransactionListenerConcurrentConsumers() {
		return fireTransactionListenerConcurrentConsumers;
	}

	public void setFireTransactionListenerConcurrentConsumers(Integer fireTransactionListenerConcurrentConsumers) {
		this.fireTransactionListenerConcurrentConsumers = fireTransactionListenerConcurrentConsumers;
	}

	public Integer getFireTransactionListenerMaxConcurrentConsumers() {
		return fireTransactionListenerMaxConcurrentConsumers;
	}

	public void setFireTransactionListenerMaxConcurrentConsumers(Integer fireTransactionListenerMaxConcurrentConsumers) {
		this.fireTransactionListenerMaxConcurrentConsumers = fireTransactionListenerMaxConcurrentConsumers;
	}

	public Integer getTransactionCheckerListenerConcurrentConsumers() {
		return transactionCheckerListenerConcurrentConsumers;
	}

	public void setTransactionCheckerListenerConcurrentConsumers(Integer transactionCheckerListenerConcurrentConsumers) {
		this.transactionCheckerListenerConcurrentConsumers = transactionCheckerListenerConcurrentConsumers;
	}

	public Integer getTransactionCheckerListenerMaxConcurrentConsumers() {
		return transactionCheckerListenerMaxConcurrentConsumers;
	}

	public void setTransactionCheckerListenerMaxConcurrentConsumers(Integer transactionCheckerListenerMaxConcurrentConsumers) {
		this.transactionCheckerListenerMaxConcurrentConsumers = transactionCheckerListenerMaxConcurrentConsumers;
	}
}
