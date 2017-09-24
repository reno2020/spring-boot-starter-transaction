package org.throwable.protocol.constants;

/**
 * @author throwable
 * @version v1.0
 * @description Queue protocol enum
 * @since 2017/9/23 10:55
 */
public enum QueueEnum {
	/**
	 * half message queue name
	 */
	HALF_MESSAGE_QUEUE("ons.transaction.half-message"),

	FIRE_TRANSACTION_QUEUE("ons.transaction.fire-transaction"),

	CHECKER_QUEUE("ons.transaction.checker");

	private String value;

	QueueEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
