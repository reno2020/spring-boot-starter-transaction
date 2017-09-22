package org.throwble.ons.client.support;


import org.throwable.protocol.constants.LocalTransactionStats;
import org.throwble.ons.client.common.model.TransactionCallbackResult;
import org.throwble.ons.client.exception.LocalTransactionExecutionException;
import org.throwble.ons.client.exception.LocalTransactionExecutionTimeoutException;
import org.throwble.ons.client.exception.LocalTransactionInterruptedException;
import org.throwble.ons.client.exception.LocalTransactionTimeoutException;

import java.util.concurrent.*;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/22 9:32
 */
public class BlockingLocalTransactionExecutorConsumer {

    private final static int DEFAULT_PREFETCH_COUNT = 1;
    private final BlockingQueue<LocalTransactionExecutor> localTransactionExecutors;

    private String transactionId;
    private long fireTransactionTimeoutSeconds;
    private long executeTransactionTimeoutSeconds;
    private String uniqueCode;
    private ExecutorService executorService;

    public BlockingLocalTransactionExecutorConsumer(long fireTransactionTimeoutSeconds,
                                                    long executeTransactionTimeoutSeconds,
                                                    String uniqueCode,
                                                    ExecutorService executor) {
        this(DEFAULT_PREFETCH_COUNT, fireTransactionTimeoutSeconds, executeTransactionTimeoutSeconds, uniqueCode, executor);
    }

    public BlockingLocalTransactionExecutorConsumer(int prefetchCount,
                                                    long fireTransactionTimeoutSeconds,
                                                    long executeTransactionTimeoutSeconds,
                                                    String uniqueCode,
                                                    ExecutorService executor) {
        this.fireTransactionTimeoutSeconds = fireTransactionTimeoutSeconds;
        this.executeTransactionTimeoutSeconds = executeTransactionTimeoutSeconds;
        this.executorService = executor;
        this.uniqueCode = uniqueCode;
        this.localTransactionExecutors = new LinkedBlockingQueue<>(prefetchCount);
    }

    public void addLocalTransactionExecutor(LocalTransactionExecutor transactionExecutor) {
        try {
            this.localTransactionExecutors.put(transactionExecutor);
        } catch (InterruptedException e) {
            throw new LocalTransactionInterruptedException("Process addLocalTransactionExecutor interrupted!", e);
        }
    }

    public TransactionCallbackResult processLocalTransactionExecutor() {
        return processLocalTransactionExecutor(fireTransactionTimeoutSeconds, executeTransactionTimeoutSeconds, TimeUnit.SECONDS);
    }

    public TransactionCallbackResult processLocalTransactionExecutor(long fireTransactionTimeoutSeconds,
                                                                     long executeTransactionTimeoutSeconds,
                                                                     TimeUnit unit) {
        LocalTransactionExecutor executor;
        try {
            executor = localTransactionExecutors.poll(fireTransactionTimeoutSeconds, unit);
        } catch (InterruptedException e) {
            throw new LocalTransactionInterruptedException(String.format("Fire transactionExecutor interrupted,transactionId:%s,uniqueCode:%s",transactionId, uniqueCode), e);
        }
        if (null == executor) {
            throw new LocalTransactionTimeoutException(String.format("Fire transactionExecutor timeout,transactionId:%s,uniqueCode:%s",transactionId, uniqueCode));
        }
        try {
            TransactionCallbackResult callbackResult = new TransactionCallbackResult();
            final CompletableFuture<LocalTransactionStats> future = new CompletableFuture<>();
            executorService.submit((Callable<Void>) () -> {
                try {
                    future.complete(executor.doInLocalTransaction());
                } catch (Exception e) {
                    future.completeExceptionally(new LocalTransactionExecutionException(e));
                }
                return null;
            });
            LocalTransactionStats localTransactionStats = future.get(executeTransactionTimeoutSeconds, TimeUnit.SECONDS);
            callbackResult.setLocalTransactionStats(localTransactionStats);
            callbackResult.setTransactionId(getTransactionId());
            return callbackResult;
        } catch (Exception e) {
            handleTransactionException(e, uniqueCode);
        }
        throw new LocalTransactionExecutionException(String.format("Process processLocalTransactionExecutor failed,transactionId:%s,uniqueCode:%s",transactionId, uniqueCode));
    }

    private void handleTransactionException(Exception e, String uniqueCode) {
        if (e instanceof InterruptedException) {
            throw new LocalTransactionExecutionException(String.format("Process processLocalTransactionExecutor interrupted,transactionId:%s,uniqueCode:%s", transactionId, uniqueCode), e);
        } else if (e instanceof ExecutionException) {
            throw new LocalTransactionExecutionException(String.format("Process processLocalTransactionExecutor failed,transactionId:%s,uniqueCode:%s", transactionId, uniqueCode), e);
        } else if (e instanceof TimeoutException) {
            throw new LocalTransactionExecutionTimeoutException(String.format("Process processLocalTransactionExecutor timeout,transactionId:%s,uniqueCode:%s",transactionId, uniqueCode), e);
        } else if (e instanceof LocalTransactionExecutionException) {
            throw new LocalTransactionExecutionException(String.format("Process processLocalTransactionExecutor failed,transactionId:%s,uniqueCode:%s",transactionId, uniqueCode), e);
        } else {
            throw new LocalTransactionExecutionException(String.format("Process processLocalTransactionExecutor failed,transactionId:%s,uniqueCode:%s",transactionId, uniqueCode), e);
        }
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }
}
