package org.throwable.ons.client.support;

import org.throwable.protocol.constants.LocalTransactionStats;

/**
 * @author throwable
 * @version v1.0
 * @description Local transaction checker
 * @since 2017/9/22 14:28
 */
public interface LocalTransactionChecker {

	LocalTransactionStats doInTransactionCheck(String messageId, String uniqueCode, Long transactionId);
}
