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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		DestinationMessagePair that = (DestinationMessagePair) o;

		if (!destinationQueue.equals(that.destinationQueue)) return false;
		if (!destinationExchange.equals(that.destinationExchange)) return false;
		if (destinationExchangeType != that.destinationExchangeType) return false;
		if (!destinationRoutingKey.equals(that.destinationRoutingKey)) return false;
		if (!destinationContent.equals(that.destinationContent)) return false;
		return headers.equals(that.headers);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + destinationQueue.hashCode();
		result = 31 * result + destinationExchange.hashCode();
		result = 31 * result + destinationExchangeType.hashCode();
		result = 31 * result + destinationRoutingKey.hashCode();
		result = 31 * result + destinationContent.hashCode();
		result = 31 * result + headers.hashCode();
		return result;
	}
}
