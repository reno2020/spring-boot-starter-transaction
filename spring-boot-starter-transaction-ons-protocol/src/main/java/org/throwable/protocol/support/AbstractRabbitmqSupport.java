package org.throwable.protocol.support;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/24 23:04
 */
public abstract class AbstractRabbitmqSupport {

	protected static final RabbitmqMessagePropertiesConverter CONVERTER = new RabbitmqMessagePropertiesConverter();
	protected static final boolean MULTIPLE_ACK = Boolean.FALSE;
}
