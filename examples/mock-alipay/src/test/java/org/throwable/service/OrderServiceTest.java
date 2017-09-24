package org.throwable.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.throwable.AlipayApplication;

import static org.junit.Assert.*;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/24 0:07
 */
@SpringBootTest(classes = AlipayApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderServiceTest {

	@Autowired
	private OrderService orderService;

	@Test
	public void findAllOrders() throws Exception {
//		System.out.println(orderService.findAllOrders());
	}

}