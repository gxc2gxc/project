package com.xuchen.project.common.util;

public class UserContext {

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setUser(Long userId) {
        threadLocal.set(userId);
    }

    public static Long getUser() {
        return threadLocal.get();
    }

    public static void removeUser() {
        threadLocal.remove();
    }
}
