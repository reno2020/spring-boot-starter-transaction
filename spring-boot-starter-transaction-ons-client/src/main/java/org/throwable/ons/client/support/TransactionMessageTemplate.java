package org.throwable.ons.client.support;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;
import org.throwable.ons.client.common.model.TransactionCallbackResult;
import org.throwable.ons.client.common.model.TransactionSendResult;
import org.throwable.ons.client.exception.SendMqMessageException;
import org.throwable.ons.core.utils.UuidUtils;
import org.throwable.protocol.constants.ExchangeEnum;
import org.throwable.protocol.constants.LocalTransactionStats;
import org.throwable.protocol.constants.SendStats;
import org.throwable.protocol.model.DestinationMessagePair;
import org.throwable.protocol.support.RabbitmqMessagePropertiesConverter;

import java.util.List;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/22 18:24
 */
@Slf4j
public class TransactionMessageTemplate implements BeanFactoryAware {

	private static final RabbitmqMessagePropertiesConverter CONVERTER = new RabbitmqMessagePropertiesConverter();
	private DefaultListableBeanFactory beanFactory;
	private TransactionThreadPoolProvider threadPoolProvider;
	private RabbitTemplate rabbitTemplate;
	private String exchangeRoutingKey;
	private String exchangeRoutingKeySuffix;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}

	public TransactionSendResult sendMessageInTransaction(@NonNull final String messageId,
														  @NonNull final LocalTransactionExecutor localTransactionExecutor,
														  @NonNull final Class<? extends LocalTransactionChecker> checkerClass,
														  long confirmTimeoutSeconds,
														  long fireTransactionTimeoutSeconds,
														  long executeTransactionTimeoutSeconds,
														  final List<DestinationMessagePair> destinationMessagePairs) {
		checkLocalTransactionCheckerValid(checkerClass);
		checkDestinationRabbitmqMessageArgumentsValid(destinationMessagePairs);
		TransactionSendResult sendResult = new TransactionSendResult();
		String uniqueCode = UuidUtils.generateUUID();
		sendResult.setSendStats(SendStats.PREPARE);
		sendResult.setMessageId(messageId);
		sendResult.setUniqueCode(uniqueCode);
		sendResult.setLocalTransactionStats(LocalTransactionStats.UNKNOWN);
		LocalTransactionExecutorAdapter adapter = new LocalTransactionExecutorAdapter(localTransactionExecutor);
		LocalTransactionExecutionSynchronizer.addTransactionExecutor(uniqueCode, adapter);
		BlockingLocalTransactionExecutorConsumer consumer = new BlockingLocalTransactionExecutorConsumer(fireTransactionTimeoutSeconds,
				executeTransactionTimeoutSeconds, uniqueCode, threadPoolProvider.getTransactionExecutor());
		LocalTransactionExecutionSynchronizer.addTransactionConsumer(uniqueCode, consumer);
		try {
			sendResult.setSendStats(rabbitTemplate.execute(channel -> {
				channel.confirmSelect();
				channel.basicPublish(exchangeRoutingKey, exchangeRoutingKey,
						CONVERTER.createMessagePropertiesAndConvertToBasicProperties(),
						CONVERTER.wrapRabbitmqMessageBody(messageId, uniqueCode, checkerClass.getName(), sendResult.getTransactionId(),
								sendResult.getLocalTransactionStats(), sendResult.getSendStats(), exchangeRoutingKeySuffix, destinationMessagePairs));
				try {
					if (channel.waitForConfirms(confirmTimeoutSeconds * 1000)) {
						return SendStats.HALF_SUCCESS;
					}
				} catch (Exception e) {
					log.error("Confirm [HALF_MESSAGE] message failed, messageId:{},uniqueCode:{}", messageId, uniqueCode, e);
					throw new SendMqMessageException(e);
				}
				return SendStats.FAIL;
			}));
			if (SendStats.HALF_SUCCESS.equals(sendResult.getSendStats())) {  //send half-message success case
				TransactionCallbackResult callbackResult = consumer.processLocalTransactionExecutor();
				sendResult.setLocalTransactionStats(callbackResult.getLocalTransactionStats());
				sendResult.setTransactionId(callbackResult.getTransactionId());
			}
			if (LocalTransactionStats.COMMITTED.equals(sendResult.getLocalTransactionStats()) ||
					LocalTransactionStats.ROLLBACK.equals(sendResult.getLocalTransactionStats())) {  //finish executing local transaction case
				sendResult.setSendStats(rabbitTemplate.execute(channel -> {

					channel.confirmSelect();
					channel.basicPublish(exchangeRoutingKey, exchangeRoutingKey,
							CONVERTER.createMessagePropertiesAndConvertToBasicProperties(),
							CONVERTER.wrapRabbitmqMessageBody(messageId, uniqueCode, checkerClass.getName(), sendResult.getTransactionId(),
									sendResult.getLocalTransactionStats(), sendResult.getSendStats(), exchangeRoutingKeySuffix, null));
					try {
						if (channel.waitForConfirms(confirmTimeoutSeconds * 1000)) {
							return SendStats.SUCCESS;
						}
					} catch (Exception e) {
						log.error("Confirm [CONFIRM_TRANSACTION_MESSAGE] message failed, messageId:{},uniqueCode:{}", messageId, uniqueCode, e);
						throw new SendMqMessageException(e);
					}
					return SendStats.FAIL;
				}));
			}
			return sendResult;
		} finally {
			LocalTransactionExecutionSynchronizer.removeTransactionConsumer(uniqueCode);
			LocalTransactionExecutionSynchronizer.removeTransactionExecutor(uniqueCode);
		}
	}

	private void checkLocalTransactionCheckerValid(Class<? extends LocalTransactionChecker> checkerClass) {
		String[] beanNamesForType = beanFactory.getBeanNamesForType(checkerClass);
		Assert.isTrue(null != beanNamesForType && 1 == beanNamesForType.length,
				"Instance of checkerClass must be a bean managed by Spring and only one could be defined!");
	}

	private void checkDestinationRabbitmqMessageArgumentsValid(List<DestinationMessagePair> destinationMessagePairs) {
		Assert.notEmpty(destinationMessagePairs, "DestinationMessagePair collection to set must not be empty!");
		destinationMessagePairs.parallelStream().forEach(each -> {
			Assert.hasText(each.getDestinationQueue(), "DestinationQueue must not be empty!");
			Assert.hasText(each.getDestinationExchange(), "DestinationExchange must not be empty!");
			Assert.notNull(each.getDestinationExchangeType(), "DestinationExchangeType must not be null!");
			if (ExchangeEnum.DIRECT.equals(each.getDestinationExchangeType()) ||
					ExchangeEnum.TOPIC.equals(each.getDestinationExchangeType())) {
				Assert.hasText(each.getDestinationRoutingKey(), "DestinationRoutingKey for [DIRECT] or [TOPIC] exchange must not be empty!");
			} else if (ExchangeEnum.HEADERS.equals(each.getDestinationExchangeType())) {
				Assert.notEmpty(each.getHeaders(), "Headers for [HEADERS] exchange must not be empty!");
			}
		});
	}

	public TransactionThreadPoolProvider getThreadPoolProvider() {
		return threadPoolProvider;
	}

	public void setThreadPoolProvider(TransactionThreadPoolProvider threadPoolProvider) {
		this.threadPoolProvider = threadPoolProvider;
	}

	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public String getExchangeRoutingKey() {
		return exchangeRoutingKey;
	}

	public void setExchangeRoutingKey(String exchangeRoutingKey) {
		this.exchangeRoutingKey = exchangeRoutingKey;
	}

	public String getExchangeRoutingKeySuffix() {
		return exchangeRoutingKeySuffix;
	}

	public void setExchangeRoutingKeySuffix(String exchangeRoutingKeySuffix) {
		this.exchangeRoutingKeySuffix = exchangeRoutingKeySuffix;
	}
}
