package com.lenabru.googlelocation.base;

import com.lenabru.googlelocation.managers.EventBusManager;

/**
 * Created by eSommerMacMini on 30/12/2017.
 */

public class BaseManager implements EventSender {
    @Override
    public <T> void sendEvent(Class<T> clz, Action<T> action) {
        EventBusManager.getInstance().sendEvent(clz,action);
    }
}
