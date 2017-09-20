package org.throwable.protocol.support;

import lombok.NonNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.throwable.protocol.model.MessageBody;

import java.util.Map;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 17:33
 */
public final class RabbitmqMessagePropertiesConverter {

    private static final MessagePropertiesConverter MESSAGE_PROPERTIES_CONVERTER = new DefaultMessagePropertiesConverter();
    private static final DefaultConversionService CONVERSION_SERVICE = new DefaultConversionService();

    public void wrapMessageProperties(@NonNull final MessageBody messageBody,
                                      @NonNull final String messageId,
                                      @NonNull final String uniqueCode,
                                      final String queue,
                                      final String exchange,
                                      final String exchangeType,
                                      final String routingKey,
                                      final String checkerClassName,
                                      final Map<String, Object> headers) {
        MessageProperties messageProperties = messageBody.getMessageProperties();
        if (null != headers && !headers.isEmpty()){
            for (Map.Entry<String,Object> entry: headers.entrySet()){
                messageProperties.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }
}
