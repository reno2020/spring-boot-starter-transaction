package org.throwable.protocol.model;

import lombok.Data;
import org.throwable.protocol.constants.ExchangeEnum;

import java.util.Map;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 23:13
 */
@Data
public class DestinationMessagePair {

	private final String destinationQueue;
	private final String destinationExchange;
	private final ExchangeEnum destinationExchangeType;
	private final String destinationRoutingKey;
	private final String destinationContent;

	private Map<String, Object> headers;

	public DestinationMessagePair(String destinationQueue,
								  String destinationExchange,
								  ExchangeEnum destinationExchangeType,
								  String destinationRoutingKey,
								  String destinationContent) {
		this.destinationQueue = destinationQueue;
		this.destinationExchange = destinationExchange;
		this.destinationExchangeType = destinationExchangeType;
		this.destinationRoutingKey = destinationRoutingKey;
		this.destinationContent = destinationContent;
	}
}
