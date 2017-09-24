package org.throwable.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.throwable.dao.OrderMapper;
import org.throwable.entity.Order;
import org.throwable.ons.client.support.LocalTransactionExecutor;
import org.throwable.protocol.constants.LocalTransactionStats;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/24 13:34
 */
@Component
public class OrderLocalTransactionExecutor implements LocalTransactionExecutor {

	@Autowired
	private OrderMapper orderMapper;

	private Order order;

	@Override
	public LocalTransactionStats doInLocalTransaction() {
		order.setOrderStatus(1);
		orderMapper.updateByPrimaryKey(order);
		return LocalTransactionStats.COMMITTED;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
