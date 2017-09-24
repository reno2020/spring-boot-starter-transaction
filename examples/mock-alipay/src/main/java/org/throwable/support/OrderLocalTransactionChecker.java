package org.throwable.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.throwable.dao.OrderMapper;
import org.throwable.entity.Order;
import org.throwable.mapper.core.support.plugins.condition.Condition;
import org.throwable.ons.client.support.LocalTransactionChecker;
import org.throwable.protocol.constants.LocalTransactionStats;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/24 13:40
 */
@Component
public class OrderLocalTransactionChecker implements LocalTransactionChecker {

	@Autowired
	private OrderMapper orderMapper;

	@Override
	public LocalTransactionStats doInTransactionCheck(String messageId, String uniqueCode, Long transactionId) {
		Condition condition = Condition.create(Order.class);
		condition.eq("orderId", messageId);
		Order order = orderMapper.selectOneByCondition(condition);
		if (null != order && 1 == order.getOrderStatus()) {
			return LocalTransactionStats.COMMITTED;
		}
		return LocalTransactionStats.ROLLBACK;
	}
}
