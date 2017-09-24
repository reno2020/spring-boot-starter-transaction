package org.throwable.protocol.constants;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/23 19:08
 */
public enum ListenerEnum {

	FIRE_TRANSACTION_LISTENER("fireTransactionListener"),

	TRANSACTION_CHECKER_LISTENER("transactionCheckListener");

	private String value;

	ListenerEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
