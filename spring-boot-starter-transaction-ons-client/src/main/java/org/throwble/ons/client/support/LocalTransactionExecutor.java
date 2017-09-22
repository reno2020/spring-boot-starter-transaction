package org.throwble.ons.client.support;


import org.throwable.protocol.constants.LocalTransactionStats;

/**
 * @author throwable
 * @version v1.0
 * @description Local transaction executor
 * @since 2017/9/22 9:28
 */
public interface LocalTransactionExecutor {

    LocalTransactionStats doInLocalTransaction();
}
