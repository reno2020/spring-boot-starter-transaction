package org.throwable.ons.server.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/24 14:16
 */
@ConfigurationProperties(prefix = OnsServerProperties.PREFIX)
public class OnsServerProperties {

	public static final String PREFIX = "ons.server";
}
