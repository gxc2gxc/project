package com.xuchen.project.delay.service;

import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.delay.dto.WorkDto;

public interface WorkService {

    /**
     * 提交延迟任务
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    ResponseResult<Long> insert(WorkDto workDto);

    /**
     * 取消延迟任务
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    ResponseResult<Object> delete(WorkDto workDto);

    /**
     * 更新任务状态
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    ResponseResult<Object> update(WorkDto workDto);
}
