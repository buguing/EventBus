package com.wellee.eventbus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.wellee.eventlib.EventBus;
import com.wellee.eventlib.Subscribe;
import com.wellee.eventlib.ThreadMode;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        startActivity(new Intent(this, SecondActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
        Toast.makeText(this, bean.toString() + "thread name: " + Thread.currentThread().getName(),
                Toast.LENGTH_SHORT).show();
    }

//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void other() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
