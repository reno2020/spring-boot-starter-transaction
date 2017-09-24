package org.throwable.ons.client.common.model;


import org.throwable.protocol.constants.LocalTransactionStats;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/7/26 17:13
 */
public class TransactionCallbackResult {

    private LocalTransactionStats localTransactionStats;
    private String transactionId;

    public TransactionCallbackResult() {
    }
    public LocalTransactionStats getLocalTransactionStats() {
        return localTransactionStats;
    }

    public void setLocalTransactionStats(LocalTransactionStats localTransactionStats) {
        this.localTransactionStats = localTransactionStats;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
