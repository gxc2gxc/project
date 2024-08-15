package com.xuchen.project.delay.listener;

import com.alibaba.fastjson.JSON;
import com.xuchen.project.delay.controller.WorkController;
import com.xuchen.project.model.delay.constant.WorkConstant;
import com.xuchen.project.model.delay.dto.WorkDto;
import com.xuchen.project.model.delay.pojo.Work;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@SuppressWarnings("ALL")
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkListener {

    private final WorkController workController;

    @RabbitListener(queues = {WorkConstant.DEFAULT_DELAY_QUEUE})
    public void sampleListener(String message) throws InterruptedException {
        Work work = JSON.parseObject(message, Work.class);
        log.info("消费者接收到消息，消息为：{}", work);

        // 处理任务前后更新任务状态
        WorkDto workDto = new WorkDto();
        BeanUtils.copyProperties(work, workDto);
        workDto.setStatus(3);
        workController.update(workDto);

        // 发起远程调用执行任务
        Thread.sleep(60 * 1000);

        // 处理任务之后更新状态
        workDto.setStatus(4);
        workController.update(workDto);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = WorkConstant.TEST_DELAY_QUEUE, durable = "true"),
            exchange = @Exchange(name = WorkConstant.TEST_DELAY_EXCHANGE, delayed = "true"),
            key = WorkConstant.TEST_DELAY_KEY
    ))
    public void testSampleListener(String message) throws InterruptedException {
        Work work = JSON.parseObject(message, Work.class);
        log.info("消费者接收到消息，消息为：{}", work);

        // 处理任务前后更新任务状态
        WorkDto workDto = new WorkDto();
        BeanUtils.copyProperties(work, workDto);
        workDto.setStatus(3);
        workController.update(workDto);

        // 发起远程调用执行任务
        Thread.sleep(60 * 1000);

        // 处理任务之后更新状态
        workDto.setStatus(5);   // 失败
        workController.update(workDto);
    }
}
