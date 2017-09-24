package org.throwable.ons.client.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.throwable.ons.client.support.BlockingLocalTransactionExecutorConsumer;
import org.throwable.ons.client.support.LocalTransactionExecutionSynchronizer;
import org.throwable.ons.core.common.constants.Constants;
import org.throwable.protocol.model.MessageBody;
import org.throwable.protocol.support.AbstractRabbitmqSupport;
import org.throwable.protocol.utils.FastJsonUtils;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/23 11:33
 */
@Slf4j
public class FireTransactionListener extends AbstractRabbitmqSupport implements ChannelAwareMessageListener {

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		String uniqueCode = null;
		String transactionId = null;
		try {
			MessageBody messageBody = FastJsonUtils.parseFromJsonString(new String(message.getBody(), Constants.ENCODING),
					MessageBody.class);
			uniqueCode = CONVERTER.getAttributeValue(messageBody, Constants.UNIQUECODE_KEY);
			transactionId = CONVERTER.getAttributeValue(messageBody, Constants.TRANSACTIONID_KEY);
			if (LocalTransactionExecutionSynchronizer.existTransactionConsumer(uniqueCode)) {
				BlockingLocalTransactionExecutorConsumer consumer = LocalTransactionExecutionSynchronizer.getTransactionConsumer(uniqueCode);
				consumer.setTransactionId(transactionId);
				if (LocalTransactionExecutionSynchronizer.existTransactionExecutor(uniqueCode)) {
					consumer.addLocalTransactionExecutor(LocalTransactionExecutionSynchronizer.getTransactionExecutor(uniqueCode));
				}
			}
		} catch (Exception e) {
			log.error("Processing [FireTransactionListener] onMessage failed,uniqueCode:{},transactionId:{}", uniqueCode, transactionId, e);
		}
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), MULTIPLE_ACK);
	}
}
