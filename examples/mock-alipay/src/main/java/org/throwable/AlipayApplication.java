package org.throwable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.throwable.entity.Order;
import org.throwable.ons.client.support.TransactionMessageTemplate;
import org.throwable.protocol.constants.ExchangeEnum;
import org.throwable.protocol.model.DestinationMessagePair;
import org.throwable.service.OrderService;
import org.throwable.support.OrderLocalTransactionChecker;
import org.throwable.support.OrderLocalTransactionExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/23 22:32
 */
@SpringBootApplication
public class AlipayApplication implements CommandLineRunner {

	@Autowired
	private TransactionMessageTemplate transactionMessageTemplate;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderLocalTransactionExecutor orderLocalTransactionExecutor;

	public static void main(String[] args) {
		SpringApplication.run(AlipayApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<DestinationMessagePair> destinationMessagePairs = new ArrayList<>();
		List<Order> orders = orderService.findAllOrders();
		for (int i = 0; i < 10; i++) {
			Order order = orders.get(i);
			destinationMessagePairs.clear();
			destinationMessagePairs.add(new DestinationMessagePair("test", "test",
					ExchangeEnum.DIRECT, "test", "test"));
			orderLocalTransactionExecutor.setOrder(order);
			transactionMessageTemplate.sendMessageInTransaction(
					order.getOrderId(),
					orderLocalTransactionExecutor,
					OrderLocalTransactionChecker.class,
					5, 5, 5,
					destinationMessagePairs
			);
		}
	}
}