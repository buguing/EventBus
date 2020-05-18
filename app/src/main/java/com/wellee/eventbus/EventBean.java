package com.wellee.eventbus;

/**
 * @author : liwei
 * 创建日期 : 2019/12/24 11:09
 * 邮   箱 : liwei@worken.cn
 * 功能描述 :
 */
public class EventBean {
    private String key;
    private String value;

    public EventBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
