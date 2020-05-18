package com.wellee.eventbus;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.wellee.eventlib.EventBus;

/**
 * @author : liwei
 * 创建日期 : 2019/12/24 11:07
 * 邮   箱 : liwei@worken.cn
 * 功能描述 :
 */
public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void send(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new EventBean("发送事件", "我是消息"));
            }
        }).run();
    }
}
