package org.throwble.ons.client.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/22 15:59
 */
@ConfigurationProperties(prefix = OnsClientProperties.PREFIX)
public class OnsClientProperties {

    public static final String PREFIX = "";
}
