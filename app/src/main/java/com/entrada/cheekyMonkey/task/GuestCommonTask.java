package com.entrada.cheekyMonkey.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.entrada.cheekyMonkey.appInterface.IAsyncTaskRunner;
import com.entrada.cheekyMonkey.asyncTask.AsyncTaskRunner;
import com.entrada.cheekyMonkey.entity.GuestOrderItem;
import com.entrada.cheekyMonkey.entity.GusetOrderDetail;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.staticData.ResultMessage;
import com.entrada.cheekyMonkey.staticData.StaticConstants;
import com.entrada.cheekyMonkey.ui.CustomAsynctaskLoader;
import com.entrada.cheekyMonkey.ui.ProgressBarCircularIndeterminate;
import com.entrada.cheekyMonkey.util.Logger;
import com.entrada.cheekyMonkey.util.UtilToCreateJSON;

/**
 * @param <Params>
 * @param <Result>
 * @author Tanuj.Sareen
 */
@SuppressWarnings("unchecked")
public class GuestCommonTask<Params, Result> extends
        AsyncTaskRunner<Params, Object, Result> {
    String UserId = "";

    public GuestCommonTask(Context context,
                           IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
                           ProgressBar Progressloader) {
        super(context, Keyword, Progressloader, asyncTaskRunner);

    }

    public GuestCommonTask(Context context,
                           IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword) {
        super(context, Keyword, asyncTaskRunner);

    }

    public GuestCommonTask(Context context,
                           IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
                           String UserId) {
        super(context, Keyword, asyncTaskRunner);
        this.UserId = UserId;
    }

    public GuestCommonTask(Context context,
                           IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
                           ProgressDialog Progressloader) {
        super(context, Keyword, Progressloader, asyncTaskRunner);
    }

    public GuestCommonTask(Context context,
                           IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
                           String paramter, CustomAsynctaskLoader loader) {
        super(context, Keyword, paramter, loader, asyncTaskRunner);
    }

    public GuestCommonTask(Context context,
                           IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
                           String paramter, ProgressBarCircularIndeterminate pb) {
        super(context, Keyword, paramter, pb, asyncTaskRunner);
    }
    @Override
    protected Result doInBackground(Params... params) {
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.STATUS = StaticConstants.ASYN_NETWORK_FAIL;
        if (!BaseNetwork.obj().checkConnOnline(context))
            return (Result) resultMessage;
        // publishProgress((Integer) StaticConstants.ASYNTASK_STARTING);

        resultMessage.TYPE = urlString;
        jsonParser = new UtilToCreateJSON();

        jsonString = BaseNetwork.obj().postMethodWay(info.getServerIP(), null,
                urlString, paramter);

        Logger.d("AuthLoginTask", "::" + jsonString);
        if (!TextUtils.isEmpty(jsonString)) {

            if (urlString.equalsIgnoreCase(BaseNetwork.KEY_ECABS_DisplayGuest)) {
                jsonParser.getGuestDetailResponse(context, jsonString,
                        resultMessage);
            } else if (urlString
                    .equalsIgnoreCase(BaseNetwork.KEY_ECABS_GuestOrder)) {
                jsonParser.getGuestOrderResponse(context, jsonString,
                        resultMessage);

            } else if (urlString
                    .equalsIgnoreCase(BaseNetwork.KEY_ECABS_DispalyGuestOrder)) {

                try {

                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsoArray = jsonObject
                            .getJSONArray(StaticConstants.JSON_TAG_GUEST_ORDER_RESULT);

                    int lenght = jsoArray.length();

                    for (int i = 0; i < lenght; i++) {
                        JSONObject jsonValue = jsoArray.getJSONObject(i);
                        GuestOrderItem item = new GuestOrderItem();

                        item.ORDER_NUM = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_ORDER_NUM);
                        item.ITEM = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_ITEM);
                        item.QTY = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_QTY);
                        item.Item_Rate = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_ITEM_RATE);
                        item.Guest_Code = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_GUEST_CODE);
                        item.Guest_Name = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_GUEST_NAME);
                        item.TABLE = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_GUEST);
                        publishProgress(item);

                    }
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (urlString
                    .equalsIgnoreCase(BaseNetwork.KEY_ECABS_DISPLAY_GUEST_ORDER_DETAIL)) {

                try {

                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsoArray = jsonObject
                            .getJSONArray(StaticConstants.JSON_TAG_GUEST_DETAIL_RESULT);

                    int lenght = jsoArray.length();
                    for (int i = 0; i < lenght; i++) {
                        JSONObject jsonValue = jsoArray.getJSONObject(i);
                        GusetOrderDetail item = new GusetOrderDetail();
                        item.ItemCode = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_ORDER_NUM);
                        item.ItemName = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_ITEM);
                        item.ItemQty = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_QTY);
                        item.ItemPrice = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_ITEM_RATE);
                        item.TotalAmt = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_ITEM_TOTAL);
                        item.Table = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_GUEST);
                        item.Order_sts = jsonValue
                                .getString(StaticConstants.JSON_TAG_GUEST_ORDER_STATUS);
//                        item.AddOn = jsonValue
//                                .getString(StaticConstants.JSON_TAG_GST_ORDER_ADDON);
                        publishProgress(item);

                    }
                    resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (urlString
                    .equalsIgnoreCase(BaseNetwork.KEY_ECABS_PushOrderGuest)) {

                resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
            } else if (urlString
                    .equalsIgnoreCase(BaseNetwork.KEY_ECABS_CancelGuestOrder)) {
                resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;

            } else if (urlString
                    .equalsIgnoreCase(BaseNetwork.KEY_ECABS_PushOrderGuestStatus)) {
                resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
            }

        } else {
            resultMessage.STATUS = 1;
            return (Result) resultMessage;
        }
        return (Result) resultMessage;
    }

    @Override
    protected void onProgressUpdate(Object... progress) {
        super.onProgressUpdate(progress);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
    }

}
