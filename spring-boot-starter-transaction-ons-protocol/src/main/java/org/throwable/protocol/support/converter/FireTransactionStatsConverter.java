package org.throwable.protocol.support.converter;

import org.springframework.core.convert.converter.Converter;
import org.throwable.protocol.constants.FireTransactionStats;

import java.util.Locale;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/21 23:21
 */
public class FireTransactionStatsConverter implements Converter<String, FireTransactionStats> {

	@Override
	public FireTransactionStats convert(String value) {
		return FireTransactionStats.valueOf(value.toUpperCase(Locale.US));
	}
}
