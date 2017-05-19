package com.entrada.cheekyMonkey.appInterface;

import android.content.Context;

public interface IAsyncTaskRunner<Progress, Result> {

    void taskStarting();

    void taskProgress(Progress progress);

    void taskCompleted(Result result);

    void taskErrorMessage(Result result);

    Context getContext();

}
