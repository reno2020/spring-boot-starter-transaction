package org.throwable.ons.server.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/22 15:26
 */
public class TransactionTemplateProvider implements BeanFactoryAware {

    private PlatformTransactionManager transactionManager;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        PlatformTransactionManager transactionManagerBean = beanFactory.getBean(PlatformTransactionManager.class);
        Assert.notNull(transactionManagerBean, "PlatformTransactionManager bean to create TransactionTemplate must not be null!");
        this.transactionManager = transactionManagerBean;
    }

    public TransactionTemplate getDefaultTransactionTemplate(String transactionName) {
        return getTransactionTemplate(transactionName,
                TransactionDefinition.PROPAGATION_REQUIRED,
                TransactionDefinition.ISOLATION_READ_COMMITTED);
    }

    public TransactionTemplate getDefaultTransactionTemplate() {
        return getTransactionTemplate(null,
                TransactionDefinition.PROPAGATION_REQUIRED,
                TransactionDefinition.ISOLATION_READ_COMMITTED);
    }

    public TransactionTemplate getTransactionTemplate(int propagationBehavior,
                                                      int isolationLevel) {
        return getTransactionTemplate(null, propagationBehavior, isolationLevel);
    }

    public TransactionTemplate getTransactionTemplate(String transactionName,
                                                      int propagationBehavior,
                                                      int isolationLevel) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(propagationBehavior);
        transactionTemplate.setIsolationLevel(isolationLevel);
        if (null != transactionName) {
            transactionTemplate.setName(transactionName);
        }
        return transactionTemplate;
    }
}
