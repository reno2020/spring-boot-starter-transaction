package org.throwable.protocol.support.converter;

import org.springframework.core.convert.converter.Converter;
import org.throwable.protocol.constants.SendStats;

import java.util.Locale;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/21 23:21
 */
public class SendStatsConverter implements Converter<String, SendStats> {

	@Override
	public SendStats convert(String value) {
		return SendStats.valueOf(value.toUpperCase(Locale.US));
	}
}
