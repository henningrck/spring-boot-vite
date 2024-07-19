package com.henningstorck.springbootvite.common.frontend;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "common.frontend")
@Data
public class FrontendProperties {
    private boolean developmentMode;
}
