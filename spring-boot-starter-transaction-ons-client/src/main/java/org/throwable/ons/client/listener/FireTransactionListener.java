package org.throwable.ons.client.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.util.Assert;
import org.throwable.ons.core.common.constants.Constants;
import org.throwable.protocol.model.MessageBody;
import org.throwable.protocol.utils.FastJsonUtils;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/23 11:33
 */
public class FireTransactionListener implements ChannelAwareMessageListener {

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		MessageBody messageBody = FastJsonUtils.parseFromJsonString(new String(message.getBody(), Constants.ENCODING),
				MessageBody.class);
		Assert.notNull(messageBody,"Fire local transaction failed for [MessageBody] callback is null!");
	}
}
