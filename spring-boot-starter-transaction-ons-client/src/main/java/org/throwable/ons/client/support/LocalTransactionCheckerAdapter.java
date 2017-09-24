package org.throwable.ons.client.support;

import lombok.extern.slf4j.Slf4j;
import org.throwable.protocol.constants.LocalTransactionStats;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/22 23:16
 */
@Slf4j
public abstract class LocalTransactionCheckerAdapter {

	private final LocalTransactionChecker delegate;

	public LocalTransactionCheckerAdapter(LocalTransactionChecker localTransactionChecker) {
		this.delegate = localTransactionChecker;
	}

	public LocalTransactionStats doInTransactionCheck(String messageId, String uniqueCode, Long transactionId) {
		LocalTransactionStats localTransactionStats;
		try {
			localTransactionStats = delegate.doInTransactionCheck(messageId, uniqueCode, transactionId);
		} catch (Exception e) {
			log.error("LocalTransactionCheckerAdapter$doInLocalTransaction throw an exception,force to return ROLLBACK!",e);
			localTransactionStats = LocalTransactionStats.ROLLBACK;
		}
		return localTransactionStats;
	}
}
