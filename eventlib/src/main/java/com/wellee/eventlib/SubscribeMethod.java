package com.wellee.eventlib;

import java.lang.reflect.Method;

/**
 * @author : liwei
 * 创建日期 : 2019/12/24 11:32
 * 邮   箱 : liwei@worken.cn
 * 功能描述 :
 */
public class SubscribeMethod {
    /**
     * 方法
     */
    private Method method;

    /**
     * 线程模式
     */
    private ThreadMode threadMode;

    /**
     * 方法参数类型
     */
    private Class<?> type;

    public SubscribeMethod(Method method, ThreadMode threadMode, Class<?> type) {
        this.method = method;
        this.threadMode = threadMode;
        this.type = type;
    }

    public Method getMethod() {
        return method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getType() {
        return type;
    }
}
