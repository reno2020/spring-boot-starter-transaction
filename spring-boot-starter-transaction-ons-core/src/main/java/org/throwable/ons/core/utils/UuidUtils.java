package org.throwable.ons.core.utils;

import java.util.UUID;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/21 1:00
 */
public abstract class UuidUtils {

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}
}
