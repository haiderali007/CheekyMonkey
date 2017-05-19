package com.entrada.cheekyMonkey.dynamic.Network_Info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.entrada.cheekyMonkey.IntroductionScreen;
import com.entrada.cheekyMonkey.dynamic.BaseFragmentActivity;
import com.entrada.cheekyMonkey.entity.UserInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {

		try {

			String status = NetworkUtil.getConnectivityStatusString(context);

			if (status.equals("No internet connection")){
				if (context instanceof IntroductionScreen)
					((IntroductionScreen)context).loadingScreen.setVisibility(View.GONE);
				else if (! UserInfo.PREV_NET_Status.equals(status))
					UserInfo.showNetFailureDialog(context);
			}
				UserInfo.PREV_NET_Status = status;


			if (context instanceof IntroductionScreen){
				if (status.equals("Wifi enabled") || status.equals("Mobile data enabled"))
					((IntroductionScreen)context).goToMainScreen();

			} else if(context instanceof BaseFragmentActivity){

				BaseFragmentActivity baseFragmentActivity = (BaseFragmentActivity)context;
				if (status.equals("No internet connection"))
					baseFragmentActivity.layout_retry.setVisibility(View.VISIBLE);
				/*else if (status.equals("Wifi enabled") || status.equals("Mobile data enabled"))
					if (baseFragmentActivity.layout_retry.getVisibility() == View.VISIBLE)
						baseFragmentActivity.showHome();*/
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}