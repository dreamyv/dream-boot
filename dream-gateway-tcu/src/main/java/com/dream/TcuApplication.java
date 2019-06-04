package com.dream;


import com.dream.config.ProjectProperties;
import com.dream.netty.TcuClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableConfigurationProperties(value = {ProjectProperties.class})
@SpringBootApplication
@EnableSwagger2
public class TcuApplication {

    @Autowired
    private ProjectProperties properties;

    public static void main(String[] args) {
        new SpringApplicationBuilder(TcuApplication.class).web(true).run(args);
    }

    /**
     * 初始化客户端
     */
    @Bean
    public TcuClient client() {
        TcuClient client = new TcuClient(properties.getServerIp(), properties.getServerPort(),properties.getReconnectMaxNum());
        return client;
    }

}

