package com.xuchen.project.model.delay.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_delay_work_log")
public class WorkLog {

    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private Long logId;

    @TableField("work_id")
    private Long workId;

    @TableField("exchange")
    private String exchange;

    @TableField("binding_key")
    private String bindingKey;

    @TableField("priority")
    private Integer priority;

    @TableField("params")
    private String params;

    @TableField("execute_time")
    private Date executeTime;

    // 任务状态，0：默认，1：已持久化，2：已入队，3：执行中，4：执行成功，5：执行失败
    @TableField("status")
    private Integer status;

    @TableField("message")
    private String message;

    @TableField(value = "create_user")
    private Long createUser;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
