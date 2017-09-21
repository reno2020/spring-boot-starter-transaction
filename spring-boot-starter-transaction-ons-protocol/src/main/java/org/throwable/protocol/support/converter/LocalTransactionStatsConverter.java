package org.throwable.protocol.support.converter;

import org.springframework.core.convert.converter.Converter;
import org.throwable.protocol.constants.LocalTransactionStats;

import java.util.Locale;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/21 23:21
 */
public class LocalTransactionStatsConverter implements Converter<String, LocalTransactionStats> {

	@Override
	public LocalTransactionStats convert(String value) {
		return LocalTransactionStats.valueOf(value.toUpperCase(Locale.US));
	}
}
