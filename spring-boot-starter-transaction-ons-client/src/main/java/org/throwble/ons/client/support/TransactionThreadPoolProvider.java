package org.throwble.ons.client.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/22 15:37
 */
public class TransactionThreadPoolProvider implements InitializingBean,DisposableBean {

    private final Integer coreSize;
    private final Integer maxSize;
    private final Integer queueCapacity;
    private final Long keepAliveSeconds;
    private static final String DEFAULT_THREAD_PREFIX = "ons-local-transaction-thread-";
    private static final RejectedExecutionHandler DEFAULT_REJECTED_EXECUTION_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger counter = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);  //force daemon mode
            thread.setName(DEFAULT_THREAD_PREFIX + counter.getAndIncrement());
            return thread;
        }
    };

    private ThreadPoolExecutor executor;

    public TransactionThreadPoolProvider(Integer coreSize, Integer maxSize, Integer queueCapacity, Long keepAliveSeconds) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.queueCapacity = queueCapacity;
        this.keepAliveSeconds = keepAliveSeconds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.executor = new ThreadPoolExecutor(coreSize, maxSize, keepAliveSeconds, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity), DEFAULT_THREAD_FACTORY, DEFAULT_REJECTED_EXECUTION_HANDLER);
    }

    @Override
    public void destroy() throws Exception {
        if (null != executor && !executor.isShutdown()){
            executor.shutdown();
        }
    }

    public ExecutorService getTransactionExecutor(){
        return this.executor;
    }
}
