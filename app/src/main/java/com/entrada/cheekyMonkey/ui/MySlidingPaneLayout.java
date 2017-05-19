package com.entrada.cheekyMonkey.ui;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by CSET on 04/05/2016.
 */
public class MySlidingPaneLayout extends SlidingPaneLayout {

    public MySlidingPaneLayout(Context context){
        super(context);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }


    public MySlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

 /*   @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }*/

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {

        if(!isOpen()) return false;

        return super.onInterceptTouchEvent(arg0);
    }
}
