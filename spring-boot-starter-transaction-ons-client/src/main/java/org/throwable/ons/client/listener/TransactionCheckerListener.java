package org.throwable.ons.client.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.throwable.ons.client.exception.SendMqMessageException;
import org.throwable.ons.client.support.LocalTransactionChecker;
import org.throwable.ons.core.common.constants.Constants;
import org.throwable.protocol.constants.LocalTransactionStats;
import org.throwable.protocol.constants.SendStats;
import org.throwable.protocol.model.MessageBody;
import org.throwable.protocol.support.AbstractRabbitmqSupport;
import org.throwable.protocol.utils.FastJsonUtils;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/23 11:33
 */
@SuppressWarnings("unchecked")
@Slf4j
public class TransactionCheckerListener extends AbstractRabbitmqSupport implements ChannelAwareMessageListener {

	private RabbitTemplate rabbitTemplate;
	private DefaultListableBeanFactory beanFactory;
	private String exchangeRoutingKey;
	private Long confirmTimeoutSeconds;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		MessageBody messageBody = FastJsonUtils.parseFromJsonString(new String(message.getBody(), Constants.ENCODING),
				MessageBody.class);
		try {
			String uniqueCode = CONVERTER.getAttributeValue(messageBody, Constants.UNIQUECODE_KEY);
			String messageId = CONVERTER.getAttributeValue(messageBody, Constants.MESSAGEID_KEY);
			Long transactionId = CONVERTER.getAttributeValue(messageBody, Constants.TRANSACTIONID_KEY, Long.class);
			String checkerClassName = CONVERTER.getAttributeValue(messageBody, Constants.CHECKERCLASSNAME_KEY);
			Class<? extends LocalTransactionChecker> checkerClazz = (Class<? extends LocalTransactionChecker>) Class.forName(checkerClassName);
			LocalTransactionChecker transactionChecker = beanFactory.getBean(checkerClazz);
			final LocalTransactionStats localTransactionStats = transactionChecker.doInTransactionCheck(messageId, uniqueCode, transactionId);
			rabbitTemplate.execute((ChannelCallback<Void>) channelToUse -> {
				messageBody.setAttribute(Constants.LOCALTRANSACTIONSTATS_KEY, localTransactionStats);
				messageBody.setAttribute(Constants.SENDSTATS_KEY, SendStats.HALF_SUCCESS);
				channelToUse.confirmSelect();
				channelToUse.basicPublish(exchangeRoutingKey, exchangeRoutingKey,
						CONVERTER.createMessagePropertiesAndConvertToBasicProperties(),
						messageBody.toRabbitmqMessageBodyBytes());
				if (!channelToUse.waitForConfirms(confirmTimeoutSeconds * 1000)) {
					throw new SendMqMessageException(String.format("LocalTransactionChecker send confirm message " +
									"failed,check transaction for <uniqueCode:%s,messageId:%s,transactionId:%s,checkerClassName:%s> failed!",
							uniqueCode, messageId, transactionId, checkerClassName));
				}
				return null;
			});
		} catch (Exception e) {
			log.error("Processing [TransactionCheckerListener] onMessage failed.", e);
		}
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), MULTIPLE_ACK);
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

	public DefaultListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(DefaultListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public Long getConfirmTimeoutSeconds() {
		return confirmTimeoutSeconds;
	}

	public void setConfirmTimeoutSeconds(Long confirmTimeoutSeconds) {
		this.confirmTimeoutSeconds = confirmTimeoutSeconds;
	}
}
