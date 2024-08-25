package com.xuchen.project.delay.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xuchen.project.delay.mapper.WorkLogMapper;
import com.xuchen.project.delay.mapper.WorkMapper;
import com.xuchen.project.delay.service.WorkService;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.exception.ServerException;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.delay.constant.WorkConstant;
import com.xuchen.project.model.delay.dto.WorkDto;
import com.xuchen.project.model.delay.pojo.Work;
import com.xuchen.project.model.delay.pojo.WorkLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WorkServiceImpl implements WorkService {

    private final WorkMapper workMapper;

    private final WorkLogMapper workLogMapper;

    private final RabbitTemplate rabbitTemplate;

    private final RedissonClient redissonClient;

    /**
     * 提交延迟任务
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<Long> insert(WorkDto workDto) {
        log.info("提交延迟任务：{}", workDto);

        // 创建任务对象并检查交换机
        Work work = new Work();
        BeanUtils.copyProperties(workDto, work);
        work.setStatus(1);
        if (work.getExchange() == null) {
            work.setExchange(WorkConstant.DEFAULT_DELAY_EXCHANGE);
            work.setBindingKey(WorkConstant.DEFAULT_DELAY_KEY);
        }
        if (work.getPriority() == null) {
            work.setPriority(0);
        }

        // 添加任务到数据库
        if (!addWorkToDatabase(work)) {
            throw new ServerException(ResponseStatus.INSERT_OR_UPDATE_DATABASE_EXCEPTION);
        }

        // 添加任务到消息队列
        if (work.getExecuteTime().getTime() <= System.currentTimeMillis() + WorkConstant.PREPARE_TIME) {
            if (!addWorkToRabbitMQ(work)) {
                throw new ServerException(ResponseStatus.INSERT_RABBITMQ_EXCEPTION);
            }
        }
        return ResponseResult.success(work.getWorkId());
    }


    /**
     * 取消延迟任务
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> delete(WorkDto workDto) {
        log.info("取消延迟任务：{}", workDto);

        // 检查任务状态
        Work work = workMapper.selectById(workDto.getWorkId());
        if (work == null) {
            throw new ServerException(ResponseStatus.WORK_NOT_RUNNING_EXCEPTION);
        }

        // 检查任务所有者
        if (!work.getCreateUser().equals(workDto.getCreateUser())) {
            throw new ServerException(ResponseStatus.OTHER_USER_WORK_EXCEPTION);
        }

        // 更新任务状态
        workDto = new WorkDto();
        BeanUtils.copyProperties(work, workDto);
        workDto.setStatus(5);
        workDto.setMessage("任务已取消");
        update(workDto);
        return ResponseResult.success();
    }

    /**
     * 更新任务状态
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> update(WorkDto workDto) {
        log.info("更新任务状态：{}", workDto);
        if (workDto == null || workDto.getWorkId() == null) {
            throw new ServerException(ResponseStatus.WORK_NOT_RUNNING_EXCEPTION);
        }

        // 检查任务所有者
        Work work = workMapper.selectById(workDto.getWorkId());
        if (!work.getCreateUser().equals(workDto.getCreateUser())) {
            throw new ServerException(ResponseStatus.OTHER_USER_WORK_EXCEPTION);
        }

        // 更新任务状态
        work.setStatus(workDto.getStatus());
        work.setMessage(workDto.getMessage());
        if (!addWorkToDatabase(work)) {
            throw new ServerException(ResponseStatus.INSERT_OR_UPDATE_DATABASE_EXCEPTION);
        }

        // TODO
        return ResponseResult.success();
    }

    /**
     * 将任务及日志添加到数据库
     *
     * @param work 任务
     * @return 添加结果
     */
    private boolean addWorkToDatabase(Work work) {
        log.info("将任务及日志添加到数据库：{}", work);
        try {
            WorkLog workLog = new WorkLog();
            BeanUtils.copyProperties(work, workLog);

            // 新增任务
            if (work.getWorkId() == null) {
                workMapper.insert(work);
                workLog.setWorkId(work.getWorkId());
                workLogMapper.insert(workLog);
            }

            // 更新任务
            else {
                if (work.getStatus() == 4 || work.getStatus() == 5) {
                    workMapper.deleteById(work.getWorkId());
                } else {
                    workMapper.updateById(work);
                }
                workLogMapper.insert(workLog);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将任务及日志添加到RabbitMQ
     *
     * @param work 任务
     * @return 添加结果
     */
    private boolean addWorkToRabbitMQ(Work work) {
        log.info("将任务添加到RabbitMQ：{}", work);
        try {
            rabbitTemplate.convertAndSend(
                    work.getExchange(),
                    work.getBindingKey(),
                    JSON.toJSONString(work),
                    message -> {
                        // 更新任务状态
                        WorkDto workDto = new WorkDto();
                        BeanUtils.copyProperties(work, workDto);
                        workDto.setStatus(2);
                        update(workDto);

                        // 设置任务延迟时间
                        message.getMessageProperties().setDelay((int) Math.max(0, work.getExecuteTime().getTime() - System.currentTimeMillis()));
                        return message;
                    }
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 定时刷新任务
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void refreshWork() {
        log.info("定时刷新任务");

        // 筛选满足条件的任务并添加到消息队列
        RLock lock = redissonClient.getLock(WorkConstant.DEFAULT_DELAY_LOCK);
        if (lock.tryLock()) {
            try {
                List<Work> works = workMapper.selectList(Wrappers.<Work>lambdaQuery()
                        .eq(Work::getStatus, 1)                 // 处于已提交状态的任务
                        .lt(Work::getExecuteTime, new Date(System.currentTimeMillis() + WorkConstant.PREPARE_TIME))   // 即将要执行的任务
                        .orderByAsc(Work::getExecuteTime)           // 按照执行时间升序
                        .orderByDesc(Work::getPriority)             // 按照优先级降序
                );
                for (Work work : works) {
                    if (!addWorkToRabbitMQ(work)) {
                        log.info("定时刷新任务失败: {}", work);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
