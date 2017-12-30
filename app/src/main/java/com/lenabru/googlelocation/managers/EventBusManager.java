package com.lenabru.googlelocation.managers;

import com.lenabru.googlelocation.base.Action;
import com.lenabru.googlelocation.base.EventSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by eSommerMacMini on 30/12/2017.
 */

public class EventBusManager implements EventSender{

    private Set<Object> eventSubscribers = new HashSet<>();
    private static EventBusManager instance;
    private EventBusManager(){

    }

    public synchronized  static EventBusManager getInstance(){
        if (instance == null){
            instance = new EventBusManager();
        }

        return instance;
    }

    public void register(Object subscriber){
        eventSubscribers.add(subscriber);
    }

    public void unregister(Object subscriber){
        eventSubscribers.remove(subscriber);
    }


    public <T> void sendEvent(Class<T> clz, Action<T> action){
        for (Object obj : eventSubscribers){
            if (clz.isAssignableFrom(obj.getClass())){
                action.run((T)obj);
            }
        }
    }

}
