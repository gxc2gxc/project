# 数据库
create database if not exists db_delay;

# 任务表
create table db_delay.tb_delay_work
(
    work_id      bigint       not null comment '任务ID',
    exchange     varchar(100) null comment '交换机名称',
    binding_key  varchar(100) null comment '绑定交换机与延迟队列',
    priority     int          null comment '任务优先级',
    params       longblob     null comment '任务参数',
    execute_time datetime     null comment '任务执行时间',
    status       int          null comment '任务状态',
    message      varchar(100) null comment '任务执行提示信息',
    create_user  bigint       null comment '任务创建者',
    create_time  datetime     null comment '任务创建时间',
    update_time  datetime     null comment '任务更新时间',
    constraint tb_delay_work_pk
        unique (work_id)
);

# 任务日志表
create table db_delay.tb_delay_work_log
(
    log_id       bigint       not null comment '日志ID',
    work_id      bigint       not null comment '任务ID',
    exchange     varchar(100) null comment '交换机名称',
    binding_key  varchar(100) null comment '绑定交换机与延迟队列',
    priority     int          null comment '任务优先级',
    params       longblob     null comment '任务参数',
    execute_time datetime     null comment '任务执行时间',
    status       int          null comment '任务状态',
    message      varchar(100) null comment '任务执行提示信息',
    create_user  bigint       null comment '任务创建者',
    create_time  datetime     null comment '任务创建时间',
    update_time  datetime     null comment '任务更新时间',
    constraint tb_delay_work_log_pk
        unique (log_id)
);