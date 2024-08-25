package com.xuchen.project.model.delay.constant;

public class WorkConstant {

    // 任务提前加载时间
    public static final Integer PREPARE_TIME = 600 * 1000;

    // 定时刷新分布式锁
    public static final String DEFAULT_DELAY_LOCK = "default.delay.refresh.lock";

    // 默认延迟交换机和队列
    public static final String DEFAULT_DELAY_QUEUE = "project.delay.queue.default";
    public static final String DEFAULT_DELAY_EXCHANGE = "project.delay.exchange.default";
    public static final String DEFAULT_DELAY_KEY = "project.delay.key.default";

    // 默认死信交换机和队列
    public static final String ERROR_DELAY_QUEUE = "project.delay.queue.error";
    public static final String ERROR_DELAY_EXCHANGE = "project.delay.exchange.error";
    public static final String ERROR_DELAY_KEY = "project.delay.key.error";

    // 自定义交换机和队列
    public static final String TEST_DELAY_QUEUE = "project.delay.queue.test";
    public static final String TEST_DELAY_EXCHANGE = "project.delay.exchange.test";
    public static final String TEST_DELAY_KEY = "project.delay.key.test";
}
