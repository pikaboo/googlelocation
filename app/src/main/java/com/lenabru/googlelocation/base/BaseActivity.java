package com.lenabru.googlelocation.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lenabru.googlelocation.managers.EventBusManager;

/**
 * Created by Lena Brusilovski on 30/12/2017.
 */

public class BaseActivity extends AppCompatActivity implements EventSender {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusManager.getInstance().register(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        EventBusManager.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusManager.getInstance().unregister(this);
    }


    @Override
    public <T> void sendEvent(Class<T> clz, Action<T> action) {
        EventBusManager.getInstance().sendEvent(clz,action);
    }
}
