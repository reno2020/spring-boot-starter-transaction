package org.throwable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.throwable.ons.client.support.TransactionMessageTemplate;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/23 22:32
 */
@SpringBootApplication
public class AlipayApplication implements CommandLineRunner{

	@Autowired
	private TransactionMessageTemplate transactionMessageTemplate;

	public static void main(String[] args) {
		SpringApplication.run(AlipayApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}