package org.throwble.ons.client.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/22 15:59
 */
@Configuration
@EnableConfigurationProperties(value = OnsClientProperties.class)
public class OnsClientConfiguration {


}
