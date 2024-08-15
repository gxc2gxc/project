package com.xuchen.project.delay.config;

import com.xuchen.project.model.delay.constant.WorkConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WorkConfig {

    @Bean
    public DirectExchange delayExchange() {
        return ExchangeBuilder.directExchange(WorkConstant.DEFAULT_DELAY_EXCHANGE)
                .delayed()  // 设置延迟交换机
                .durable(true)
                .build();
    }

    @Bean
    public Queue delayQueue() {
        return new Queue(WorkConstant.DEFAULT_DELAY_QUEUE);
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(WorkConstant.DEFAULT_DELAY_KEY);
    }
}