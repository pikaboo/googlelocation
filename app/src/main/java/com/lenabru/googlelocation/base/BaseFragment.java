package com.lenabru.googlelocation.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.lenabru.googlelocation.managers.EventBusManager;

/**
 * Created by eSommerMacMini on 30/12/2017.
 */

public class BaseFragment extends Fragment implements EventSender {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusManager.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusManager.getInstance().unregister(this);
    }

    @Override
    public <T> void sendEvent(Class<T> clz, Action<T> action) {
        EventBusManager.getInstance().sendEvent(clz,action);
    }
}
