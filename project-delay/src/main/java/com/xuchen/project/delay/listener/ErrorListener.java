package com.xuchen.project.delay.listener;

import com.alibaba.fastjson.JSON;
import com.xuchen.project.model.delay.constant.WorkConstant;
import com.xuchen.project.model.delay.pojo.Work;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorListener {

    @RabbitListener(queues = {WorkConstant.ERROR_DELAY_QUEUE})
    public void errorListener(String message) {
        Work work = JSON.parseObject(message, Work.class);
        log.info("任务执行失败，参数为：{}", work);
    }
}
