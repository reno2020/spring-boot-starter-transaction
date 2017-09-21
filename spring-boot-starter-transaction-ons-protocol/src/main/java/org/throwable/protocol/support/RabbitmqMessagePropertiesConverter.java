package org.throwable.protocol.support;

import com.rabbitmq.client.AMQP;
import lombok.NonNull;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.throwable.ons.core.common.constants.Constants;
import org.throwable.ons.core.utils.UuidUtils;
import org.throwable.protocol.model.DestinationMessagePair;
import org.throwable.protocol.model.MessageBody;

import java.util.List;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 17:33
 */
public final class RabbitmqMessagePropertiesConverter {

	private static final MessagePropertiesConverter MESSAGE_PROPERTIES_CONVERTER = new DefaultMessagePropertiesConverter();
	private static final DefaultConversionService CONVERSION_SERVICE = new DefaultConversionService();

	public AMQP.BasicProperties convertToBasicProperties(MessageProperties messageProperties) {
		return MESSAGE_PROPERTIES_CONVERTER.fromMessageProperties(messageProperties, Constants.ENCODING);
	}

	public byte[] wrapRabbitmqMessageBody(@NonNull final String messageId,
										  @NonNull final String checkerClassName,
										  @NonNull final List<DestinationMessagePair> destinationMessagePairs) {
		MessageBody messageBody = new MessageBody();
		messageBody.setContents(destinationMessagePairs);
		messageBody.addAttribute(Constants.UNIQUECODE_KEY, UuidUtils.generateUUID());
		messageBody.addAttribute(Constants.MESSAGEID_KEY, messageId);
		messageBody.addAttribute(Constants.CHECKERCLASSNAME_KEY, checkerClassName);
		return messageBody.toRabbitmqMessageBodyBytes();
	}

	public String getAttributeValue(MessageBody messageBody, String key) {
		Object value = messageBody.getAttribute(key);
		if (null == value) {
			return null;
		} else {
			return CONVERSION_SERVICE.convert(value, String.class);
		}
	}

	public String getAttributeValue(MessageBody messageBody, String key, String def) {
		Object value = messageBody.getAttribute(key);
		return null != value ? CONVERSION_SERVICE.convert(value, String.class) : def;

	}

	public <T> T getAttributeValue(MessageBody messageBody, String key, Class<T> clazz) {
		Object value = messageBody.getAttribute(key);
		if (null == value) {
			return null;
		} else {
			return CONVERSION_SERVICE.convert(value, clazz);
		}
	}

	public <T> T getAttributeValue(MessageBody messageBody, String key, Class<T> clazz, T def) {
		Object value = messageBody.getAttribute(key);
		return null != value ? CONVERSION_SERVICE.convert(value, clazz) : def;
	}
}
