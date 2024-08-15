package com.xuchen.project.api.delay;

import com.xuchen.project.api.delay.fallback.WorkClientFallback;
import com.xuchen.project.model.common.validation.ValidationGroup;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.delay.dto.WorkDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "project-delay", fallback = WorkClientFallback.class)
public interface WorkClient {

    /**
     * 提交延迟任务
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    @PostMapping("/delay/work/push")
    ResponseResult<Long> insert(@RequestBody @Validated({ValidationGroup.Insert.class}) WorkDto workDto);

    /**
     * 取消延迟任务
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    @DeleteMapping("/delay/work/cancel")
    ResponseResult<Object> delete(@RequestBody @Validated({ValidationGroup.Delete.class}) WorkDto workDto);

    /**
     * 更新任务状态
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    @PostMapping("/delay/work/update")
    ResponseResult<Object> update(@RequestBody @Validated({ValidationGroup.Update.class}) WorkDto workDto);
}
