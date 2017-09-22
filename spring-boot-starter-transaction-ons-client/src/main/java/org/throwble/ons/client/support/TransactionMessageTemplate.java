package org.throwble.ons.client.support;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.throwable.protocol.model.DestinationMessagePair;
import org.throwable.protocol.support.RabbitmqMessagePropertiesConverter;
import org.throwble.ons.client.common.model.TransactionSendResult;

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

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public TransactionSendResult sendMessageInTransaction(@NonNull final String messageId,
                                                          @NonNull final LocalTransactionExecutor localTransactionExecutor,
                                                          @NonNull final Class<? extends LocalTransactionChecker> checkerClass,
                                                          final List<DestinationMessagePair> destinationMessagePairs){
        TransactionSendResult result = new TransactionSendResult();

        return result;
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
}
