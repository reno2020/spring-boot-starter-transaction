package org.throwable.ons.client.support;

import lombok.extern.slf4j.Slf4j;
import org.throwable.protocol.constants.LocalTransactionStats;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/22 23:19
 */
@Slf4j
public final class LocalTransactionExecutorAdapter {

	private final LocalTransactionExecutor delegate;

	public LocalTransactionExecutorAdapter(LocalTransactionExecutor delegate) {
		this.delegate = delegate;
	}

	public LocalTransactionStats doInLocalTransaction() {
		LocalTransactionStats localTransactionStats;
		try {
			localTransactionStats = delegate.doInLocalTransaction();
		} catch (Exception e) {
			log.error("LocalTransactionExecutorAdapter$doInLocalTransaction throw an exception,force to return ROLLBACK!",e);
			localTransactionStats = LocalTransactionStats.ROLLBACK;
		}
		return localTransactionStats;
	}
}
