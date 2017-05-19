package com.entrada.cheekyMonkey.util;

import android.os.Handler;
import android.os.Message;

public class TimeHandler extends Handler {

    Runnable myRunnable;

    public TimeHandler(Runnable runnable) {
        this.myRunnable = runnable;
    }

    @Override
    public void handleMessage(Message msg) {

        myRunnable.run();

    }

    public void sleep(long delayMilliSec) {
        this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), delayMilliSec);

    }

}
