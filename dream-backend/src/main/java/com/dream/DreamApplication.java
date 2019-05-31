package com.dream;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.dream.config.ProjectProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableConfigurationProperties(value = {ProjectProperties.class})
@SpringBootApplication
@EnableSwagger2
public class DreamApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DreamApplication.class).web(true).run(args);
    }
}

