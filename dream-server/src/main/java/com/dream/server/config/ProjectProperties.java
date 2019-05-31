package com.dream.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = ProjectProperties.PREFIX)
public class ProjectProperties {
    //引用前缀
    public static final String PREFIX = "project";

}
