package org.throwable.ons.orm.entity;

import org.throwable.mapper.core.common.annotation.Column;
import org.throwable.mapper.core.common.annotation.Table;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 12:14
 */
@Table(value = "t_transaction_message")
public class TransactionMessage extends BaseEntity {

	@Column(value = "transaction_id")
	private Long transactionId;

	@Column(value = "message_id")
	private String messageId;

	@Column(value = "unique_code")
	private String uniqueCode;

	@Column(value = "destination_content")
	private String destinationContent;

}
