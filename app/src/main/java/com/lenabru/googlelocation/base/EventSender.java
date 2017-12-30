package com.lenabru.googlelocation.base;

import com.lenabru.googlelocation.managers.EventBusManager;

/**
 * Created by Lena Brusilovski on 30/12/2017.
 */

public interface EventSender {
    <T> void sendEvent(Class<T> clz, Action<T> action);
}
