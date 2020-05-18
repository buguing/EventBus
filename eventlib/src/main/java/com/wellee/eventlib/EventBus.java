package com.wellee.eventlib;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : liwei
 * 创建日期 : 2019/12/24 11:16
 * 邮   箱 : liwei@worken.cn
 * 功能描述 :
 */
public class EventBus {

    private static volatile EventBus INSTANCE;

    private Map<Object, List<SubscribeMethod>> mCacheMap;

    private Handler mHandler;

    private ExecutorService mExecutorService;

    private EventBus(){
        mCacheMap = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
        mExecutorService = Executors.newCachedThreadPool();
    }

    public static EventBus getDefault() {
        if(INSTANCE == null) {
            synchronized (EventBus.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EventBus();
                }
            }
        }
        return INSTANCE;
    }

    public void register(Object object) {
        List<SubscribeMethod> subscribeMethods = mCacheMap.get(object);
        if (subscribeMethods == null) {
            subscribeMethods = findSubscribeMethods(object);
            mCacheMap.put(object, subscribeMethods);
        }
    }

    private List<SubscribeMethod> findSubscribeMethods(Object object) {
        List<SubscribeMethod> subscribeMethods = new ArrayList<>();
        Class<?> clazz = object.getClass();

        while (clazz != null) {

            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe == null) {
                    continue;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("argument number must be one");
                }
                ThreadMode threadMode = subscribe.threadMode();
                SubscribeMethod subscribeMethod = new SubscribeMethod(method, threadMode, parameterTypes[0]);
                subscribeMethods.add(subscribeMethod);
            }
            clazz = clazz.getSuperclass();
        }
        return subscribeMethods;
    }

    public void unregister(Object object) {
        mCacheMap.remove(object);
    }

    public void post(final Object type) {
        Set<Object> keySet = mCacheMap.keySet();
        for (final Object object : keySet) {
            List<SubscribeMethod> subscribeMethods = mCacheMap.get(object);
            assert subscribeMethods != null;
            for (final SubscribeMethod subscribeMethod : subscribeMethods) {
                if (subscribeMethod.getType().isAssignableFrom(type.getClass())) {
                    ThreadMode threadMode = subscribeMethod.getThreadMode();
                    switch (threadMode) {
                        case MAIN:
                            if(Looper.myLooper() == Looper.getMainLooper()) {
                                // 主线程 -- 主线程
                                invoke(subscribeMethod, object, type);
                            } else {
                                // 子线程 -- 主线程
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribeMethod, object, type);
                                    }
                                });
                            }
                            break;
                        case BACKGROUND:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                // 主线程 -- 子线程
                                mExecutorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribeMethod, object, type);
                                    }
                                });
                            } else {
                                // 子线程 -- 子线程
                                invoke(subscribeMethod, object, type);
                            }
                            break;
                    }
                }
            }
        }
    }

    private void invoke(SubscribeMethod subscribeMethod, Object object, Object type) {
        Method method = subscribeMethod.getMethod();
        try {
            method.invoke(object, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
