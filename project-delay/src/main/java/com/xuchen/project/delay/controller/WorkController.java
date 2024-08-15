package com.xuchen.project.delay.controller;

import com.xuchen.project.api.delay.WorkClient;
import com.xuchen.project.delay.service.WorkService;
import com.xuchen.project.model.common.validation.ValidationGroup;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.delay.dto.WorkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delay/work")
public class WorkController implements WorkClient {

    private final WorkService workService;

    /**
     * 提交延迟任务
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    @Override
    @PostMapping("/push")
    public ResponseResult<Long> insert(@RequestBody @Validated({ValidationGroup.Insert.class}) WorkDto workDto) {
        return workService.insert(workDto);
    }

    /**
     * 取消延迟任务
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    @Override
    @DeleteMapping("/cancel")
    public ResponseResult<Object> delete(@RequestBody @Validated({ValidationGroup.Delete.class}) WorkDto workDto) {
        return workService.delete(workDto);
    }

    /**
     * 更新任务状态
     *
     * @param workDto 延迟任务Dto
     * @return 执行结果
     */
    @Override
    @PostMapping("/update")
    public ResponseResult<Object> update(@RequestBody @Validated({ValidationGroup.Update.class}) WorkDto workDto) {
        return workService.update(workDto);
    }
}
