package com.entrada.cheekyMonkey.dynamic;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.entrada.cheekyMonkey.entity.CategoryItem;
import com.entrada.cheekyMonkey.entity.GroupItems;
import com.entrada.cheekyMonkey.entity.Items;
import com.entrada.cheekyMonkey.entity.MenuItem;
import com.entrada.cheekyMonkey.entity.UserInfo;
import com.entrada.cheekyMonkey.network.BaseNetwork;
import com.entrada.cheekyMonkey.util.Logger;

/**
 * Created by Rahul on 05/06/2015.
 */
public class FetchMenuItemsTask extends AsyncTask<String,  ArrayList<Items>,  ArrayList<Items>> {

    private ProgressBar mDialog;
    private Context context;
    private String parameter;
    private String serverIP;
    ICalItemsResponse iCallResponse ;

    public FetchMenuItemsTask(Context context, String parameter, String serverIP,
                              ProgressBar mDialog, ICalItemsResponse iCallResponse) {

        this.context = context;
        this.mDialog = mDialog;
        this.parameter = parameter;
        this.serverIP = serverIP;
        this.iCallResponse = iCallResponse;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mDialog != null)
            mDialog.setVisibility(View.VISIBLE);
    }


    @Override
    protected ArrayList<Items> doInBackground(String... params) {

        String response = BaseNetwork.obj().postMethodWay(serverIP, "", BaseNetwork.FETCH_DYNAMIC_ITEM, parameter);
        Logger.i("Dynamic_Itm_Rate_All_List", response);

        if (! response.contains("Dynamic_Itm_Rate_AllResult")){
            return null;
        }


        ArrayList<Items> TITLES = new ArrayList<>();
        String previous_cat = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("Dynamic_Itm_Rate_AllResult");

            for (int i=0; i<jsonArray.length(); i++){

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

              /*DynamicItem dynamicItem = new DynamicItem();
                dynamicItem.Current_Rate  = jsonObject1.getString("Current_Rate");
                dynamicItem.Inc_Qty  = jsonObject1.getString("Inc_Qty");
                dynamicItem.Inc_Rate  = jsonObject1.getString("Inc_Rate");
                dynamicItem.Item_Desc = jsonObject1.getString("Item_Name");
                dynamicItem.Max_Price  = jsonObject1.getString("Max_Price");
                dynamicItem.Min_Price  = jsonObject1.getString("Min_Price");
                dynamicItem.Previous_Rate = jsonObject1.getString("Previous_Rate");
                dynamicItem.Sold_Qty  = jsonObject1.getString("Sold_Qty");
                dynamicItem.Today_Max  = jsonObject1.getString("Today_Max");
                dynamicItem.Cat_Code = jsonObject1.getString("RMNU_CAT");
                dynamicItem.Cat_Desc = jsonObject1.getString("CU_DESC");
                dynamicItem.Item_Code =jsonObject1.getString("Item_Code");*/


                if (! previous_cat.equals(jsonObject1.getString("RMNU_CAT"))) {

                    CategoryItem categoryItem = new CategoryItem();
                    categoryItem.setCategory_Code(jsonObject1.getString("RMNU_CAT"));
                    categoryItem.setCategory_Name(jsonObject1.getString("CU_DESC"));
                    categoryItem.setCategory_Image_Url(BaseNetwork.defaultUrlMethod(serverIP, "/Image/" + categoryItem.getCategory_Code() + ".png"));


                    ArrayList<MenuItem> menuItemList = new ArrayList<>();
                    MenuItem menuItem = new MenuItem();
                    menuItem.setMenu_code(jsonObject1.getString("Item_Code"));
                    menuItem.setMenu_name(jsonObject1.getString("Item_Name"));
                    menuItem.setInc_Rate(Float.parseFloat(jsonObject1.getString("Inc_Rate")));
                    menuItem.setMenu_price(Float.parseFloat(jsonObject1.getString("Current_Rate")));
                    menuItem.setMax_Price(Float.parseFloat(jsonObject1.getString("Max_Price")));
                    menuItem.setMin_Price(Float.parseFloat(jsonObject1.getString("Min_Price")));
                    menuItemList.add(menuItem);

                    TITLES.add(new Items(new GroupItems(), categoryItem, menuItemList));
                    previous_cat = jsonObject1.getString("RMNU_CAT");

                }else {

                    ArrayList<MenuItem> menuItemList = TITLES.get(TITLES.size()-1).getMenuItemList();
                    MenuItem menuItem = new MenuItem();
                    menuItem.setMenu_code(jsonObject1.getString("Item_Code"));
                    menuItem.setMenu_name(jsonObject1.getString("Item_Name"));
                    menuItem.setInc_Rate(Float.parseFloat(jsonObject1.getString("Inc_Rate")));
                    menuItem.setMenu_price(Float.parseFloat(jsonObject1.getString("Current_Rate")));
                    menuItem.setMax_Price(Float.parseFloat(jsonObject1.getString("Max_Price")));
                    menuItem.setMin_Price(Float.parseFloat(jsonObject1.getString("Min_Price")));
                    menuItemList.add(menuItem);
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return TITLES;
    }


    @Override
    protected void onPostExecute( ArrayList<Items> result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.setVisibility(View.GONE);

        if (result == null)
            UserInfo.showNetFailureDialog(context);
        iCallResponse.getItemsResponse(result);
    }
}