package com.neu.his.cloud.service.dms.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "his.order.cancel";

    @Bean
    public Queue miaoShaQueue(){
        return new Queue(MIAOSHA_QUEUE,true);
    }
}
