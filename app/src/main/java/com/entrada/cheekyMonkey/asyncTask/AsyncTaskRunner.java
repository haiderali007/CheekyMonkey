package com.entrada.cheekyMonkey.asyncTask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

import com.entrada.cheekyMonkey.Exception.SearchException;
import com.entrada.cheekyMonkey.POSApplication;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.appInterface.IAsyncTaskRunner;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.staticData.ResultMessage;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.ui.CustomAsynctaskLoader;
import com.entrada.cheekyMonkey.ui.ProgressBarCircularIndeterminate;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

/**
 * @param <Parems>
 * @param <Progress>
 * @param <Result>
 * @author Tanuj.Sareen
 */
public abstract class AsyncTaskRunner<Parems, Progress, Result> extends
        AsyncTask<Parems, Progress, Result> {

    protected UserInfo info;
    protected String paramter;

    protected String urlString;
    protected ProgressDialog pDialog;
    protected CustomAsynctaskLoader loader;

    /**
     * Custom Progress Bar
     */
    protected ProgressBarCircularIndeterminate pb;

    protected ProgressBar pBar;
    protected Context context;
    protected String jsonString = "";
    protected UtilToCreateJSON jsonParser;
    private WeakReference<Context> contextWeakRef;
    private IAsyncTaskRunner<Progress, Result> _asyncTaskRunner;

    public AsyncTaskRunner(Context context,
                           IAsyncTaskRunner<Progress, Result> asyncTaskRunner) {
        _asyncTaskRunner = asyncTaskRunner;
        contextWeakRef = new WeakReference<Context>(
                asyncTaskRunner.getContext());
        this.context = context;
        this.info = POSApplication.getSingleton().getmDataModel().getUserInfo();
    }

    public AsyncTaskRunner(Context context, String urlString,
                           IAsyncTaskRunner<Progress, Result> asyncTaskRunner) {
        _asyncTaskRunner = asyncTaskRunner;
        this.urlString = urlString;
        this.context = context;
        this.info = POSApplication.getSingleton().getmDataModel().getUserInfo();
        contextWeakRef = new WeakReference<Context>(
                asyncTaskRunner.getContext());
    }

    public AsyncTaskRunner(Context context, String urlString, ProgressBar pBar,
                           IAsyncTaskRunner<Progress, Result> asyncTaskRunner) {
        _asyncTaskRunner = asyncTaskRunner;
        this.context = context;
        this.urlString = urlString;
        this.pBar = pBar;
        this.info = POSApplication.getSingleton().getmDataModel().getUserInfo();
        contextWeakRef = new WeakReference<Context>(context);

    }

    public AsyncTaskRunner(Context context, String urlString,
                           ProgressDialog pDialog,
                           IAsyncTaskRunner<Progress, Result> asyncTaskRunner) {
        _asyncTaskRunner = asyncTaskRunner;
        this.context = context;
        this.urlString = urlString;
        this.pDialog = pDialog;
        this.info = POSApplication.getSingleton().getmDataModel().getUserInfo();
        contextWeakRef = new WeakReference<Context>(context);
    }

    public AsyncTaskRunner(Context context, String urlString, String paramter,
                           CustomAsynctaskLoader loader,
                           IAsyncTaskRunner<Progress, Result> asyncTaskRunner) {
        _asyncTaskRunner = asyncTaskRunner;
        this.context = context;
        this.urlString = urlString;
        this.paramter = paramter;
        this.loader = loader;
        this.info = POSApplication.getSingleton().getmDataModel().getUserInfo();
        contextWeakRef = new WeakReference<Context>(context);

    }

    public AsyncTaskRunner(Context context, String urlString, String paramter,
                           ProgressBarCircularIndeterminate pb,
                           IAsyncTaskRunner<Progress, Result> asyncTaskRunner) {
        _asyncTaskRunner = asyncTaskRunner;
        this.context = context;
        this.urlString = urlString;
        this.paramter = paramter;
        this.pb = pb;
        this.info = POSApplication.getSingleton().getmDataModel().getUserInfo();
        contextWeakRef = new WeakReference<Context>(context);

    }

    @SuppressWarnings("unchecked")
    @Override
    protected Result doInBackground(Parems... params) {
        return (Result) new ResultMessage();
    }

    @Override
    protected void onPreExecute() {
        _asyncTaskRunner.taskStarting();

        if (pb != null)
            pb.setVisibility(View.VISIBLE);
        if (pBar != null)
            pBar.setVisibility(View.VISIBLE);
        if (loader != null)
            loader.ShowDialog();
        if (pDialog != null)
            pDialog.show();
    }

    @Override
    protected void onPostExecute(Result result) {

        if (contextWeakRef.get() != null) {
            ResultMessage resultMessage = (ResultMessage) result;
            if (resultMessage.STATUS == StaticConstants.ASYN_NETWORK_FAIL
                    || resultMessage.STATUS == StaticConstants.ASYN_RESULT_PARSE_FAILED) {

                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                if (pBar != null)
                    pBar.setVisibility(View.GONE);
                if (loader != null)
                    loader.DismissDialog();

                if (pb != null)
                    pb.setVisibility(View.GONE);

                if (resultMessage.STATUS == StaticConstants.ASYN_NETWORK_FAIL)
                    BaseNetwork.obj().showErrorNetwork(
                            context, context.getResources().getString(R.string.error_net_failure));
                else
                    BaseNetwork.obj().showErrorNetworkDialog(
                            context, context.getResources().getString(R.string.error_dialog_error));

            } else if (resultMessage.STATUS == SearchException.NO_RESULTS) {

                if (loader != null)
                    loader.DismissDialog();
                BaseNetwork.obj().showErrorNetworkDialog(
                        context, context.getResources().getString(R.string.error_dialog_error));

            } else if (resultMessage.STATUS == SearchException.SERVER_DELAY) {

                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                if (pBar != null && pBar.isShown())
                    pBar.setVisibility(View.GONE);
                if (loader != null)
                    loader.DismissDialog();

                if (pb != null)
                    pb.setVisibility(View.GONE);

                if (context != null)
                    BaseNetwork.obj().showErrorNetworkDialog(
                            context,
                            context.getResources().getString(
                                    R.string.error_dialog_server_error));

            } else if (resultMessage.STATUS == SearchException.ERROR) {
                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                if (pBar != null)
                    pBar.setVisibility(View.GONE);

                if (loader != null)
                    loader.DismissDialog();

                if (pb != null)
                    pb.setVisibility(View.GONE);

                BaseNetwork.obj().showErrorNetworkDialog(
                        context, context.getResources().getString(
                                R.string.error_dialog_no_results));

            } else if (resultMessage.STATUS == StaticConstants.ASYN_RESULT_OK) {
                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                if (pBar != null)
                    pBar.setVisibility(View.GONE);

                if (loader != null)
                    loader.DismissDialog();

                if (pb != null)
                    pb.setVisibility(View.GONE);

                _asyncTaskRunner.taskCompleted(result);

            } else if (resultMessage.STATUS == StaticConstants.ASYN_RESULT_CANCEL) {
                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                if (pBar != null)
                    pBar.setVisibility(View.GONE);

                if (loader != null)
                    loader.DismissDialog();

                if (pb != null)
                    pb.setVisibility(View.GONE);

                showErrorNetworkDialog(context, result);

            }
        }

    }

    @Override
    protected void onProgressUpdate(
            @SuppressWarnings("unchecked") Progress... progress) {

        _asyncTaskRunner.taskProgress(progress[0]);
        if (pDialog != null)
            if (!pDialog.isShowing()) {
                pDialog.show();

            }
        if (pBar != null) {
            pBar.setVisibility(View.GONE);
            Log.i("TCV", "Show progres bar");
        }

        if (loader != null)
            //loader.ShowDialog();
            loader.DismissDialog();

        if (pb != null)
            pb.setVisibility(View.GONE);

		/*
         * if (progress[0].equals(StaticConstants.ASYNTASK_STARTING)) { if
		 * (pDialog != null) if (!pDialog.isShowing()) { pDialog.show();
		 * 
		 * } if (pBar != null) { pBar.setVisibility(View.VISIBLE); Log.i("TCV",
		 * "Show progres bar"); } }
		 */
    }

    public void showErrorNetworkDialog(final Context c, final Result result) {
        ResultMessage messageresult = (ResultMessage) result;
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(messageresult.ERRORMESSAGE).setNegativeButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        _asyncTaskRunner.taskErrorMessage(result);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}