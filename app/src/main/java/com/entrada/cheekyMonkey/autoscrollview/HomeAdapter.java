package com.entrada.cheekyMonkey.autoscrollview;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;

public class HomeAdapter extends PagerAdapter {
    private static int TOTAL = 0;
    ImageView view;
    Activity activity;
    ArrayList<String> imageurl;
    int imageArray[];
    AutoScrollViewPager autoscroll;

    public HomeAdapter(Activity act, int[] imgArra, AutoScrollViewPager autoscroll,
                       int noOfScreens) {
        imageArray = imgArra;
        activity = act;
        this.autoscroll = autoscroll;
        imageurl = new ArrayList<>();
        for (int i=0; i<noOfScreens; i++)
            imageurl.add("");
        TOTAL = imageurl.size();
    }


    public int getCount() {
        //return Integer.MAX_VALUE;
        return imageurl.size();
    }

    @SuppressWarnings("deprecation")
    public Object instantiateItem(View collection, int position) {
        position = position % TOTAL;
        view = new ImageView(activity);
        view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        view.setScaleType(ScaleType.FIT_XY);
        Bitmap bitmap;
        Drawable drawable = view.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
            bitmap = null;
        }
        bitmap = BitmapFactory.decodeResource(activity.getResources(),
                imageArray[position]);
        view.setImageDrawable(new BitmapDrawable(activity.getResources(),
                bitmap));

        ((ViewPager) collection).addView(view);
        view.setTag(imageurl.get(position));

        /*view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse((String) v
                        .getTag()));
                activity.startActivity(i);
            }
        });*/


        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // autoscroll.onTouchEvent(event);
                autoscroll.motioneventcall(event);
                return false;
            }
        });
        return view;
    }

    @Override
    public void destroyItem(View collection, int position, Object o) {
        View view = (View) o;
        ((ViewPager) collection).removeView(view);
        view = null;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void startUpdate(View arg0) {
        // TODO Auto-generated method stub

    }
}
