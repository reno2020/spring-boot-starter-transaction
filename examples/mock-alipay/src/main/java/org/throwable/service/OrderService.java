package org.throwable.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.throwable.dao.OrderMapper;
import org.throwable.entity.Order;
import org.throwable.mapper.core.support.plugins.condition.Condition;

import java.util.List;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/23 23:25
 */
@Service
public class OrderService {

	@Autowired
	private OrderMapper orderMapper;

	public List<Order> findAllOrders() {
		Condition condition = Condition.create(Order.class);
		condition.gt("id", 0L);
		return orderMapper.selectByCondition(condition);
	}
}
