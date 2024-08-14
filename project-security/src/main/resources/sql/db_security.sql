# 数据库
create database if not exists db_security;

# 用户表
create table db_security.tb_security_user
(
    user_id     bigint       not null comment '用户ID',
    username    varchar(50)  not null comment '用户名',
    password    varchar(200) not null comment '密码',
    status      int          not null comment '状态',
    create_time datetime     not null comment '创建时间',
    update_time datetime     not null comment '更新时间',
    constraint tb_security_user_pk
        unique (user_id),
    constraint tb_security_user_pk_2
        unique (username)
);

# 角色表
create table db_security.tb_security_role
(
    role_id     bigint      not null comment '角色ID',
    role_name   varchar(50) not null comment '角色名',
    status      int         not null comment '状态',
    create_time datetime    not null comment '创建时间',
    update_time datetime    not null comment '更新时间',
    constraint tb_security_role_pk
        unique (role_id),
    constraint tb_security_role_pk_2
        unique (role_name)
);

# 权限表
create table db_security.tb_security_permission
(
    permission_id   bigint      not null comment '权限ID',
    permission_name varchar(50) not null comment '权限名',
    status          int         not null comment '状态',
    create_time     datetime    not null comment '创建时间',
    update_time     datetime    not null comment '更新时间',
    constraint tb_security_permission_pk
        unique (permission_id),
    constraint tb_security_permission_pk_2
        unique (permission_name)
);

# 用户角色表
create table db_security.tb_security_user_role
(
    user_id bigint not null comment '用户ID',
    role_id bigint not null comment '角色ID',
    constraint tb_security_user_role_pk
        unique (user_id, role_id)
);

# 角色权限表
create table db_security.tb_security_role_permission
(
    role_id       bigint not null comment '角色ID',
    permission_id bigint not null comment '权限ID',
    constraint tb_security_role_permission_pk
        unique (role_id, permission_id)
);
