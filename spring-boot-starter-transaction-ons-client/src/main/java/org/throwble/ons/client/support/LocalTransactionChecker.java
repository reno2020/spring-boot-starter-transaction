package org.throwble.ons.client.support;

import org.springframework.amqp.core.Message;
import org.throwable.protocol.constants.LocalTransactionStats;

/**
 * @author throwable
 * @version v1.0
 * @description Local transaction checker
 * @since 2017/9/22 14:28
 */
public interface LocalTransactionChecker {

    LocalTransactionStats doInTransactionCheck(Message message);
}
