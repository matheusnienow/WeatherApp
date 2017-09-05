package com.hbsis.treinamento.matheus.nienow.weatherapp.components;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class FabListView extends ListView {
    private float downY = 0;
    private boolean consumeTouchEvents = false;
    private FloatingActionButton fab;

    public FabListView(Context context) {
        super(context);
    }
    public FabListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public FabListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //Attach floating action button to listen to list's scroll
    public void attachFabForScroll(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean isHandled = true;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (downY == 0)
                    downY = ev.getY();

                float distance = downY - ev.getY();
                if (!consumeTouchEvents && Math.abs(distance) > 0) {
                    isHandled = super.onTouchEvent(ev);
                    handleScroll(distance);
                }

                break;
            case MotionEvent.ACTION_DOWN:
                consumeTouchEvents = false;
                downY = ev.getY();
            default:
                if (!consumeTouchEvents)
                    isHandled = super.onTouchEvent(ev);
                break;
        }

        return isHandled;
    }

    private void handleScroll(float distance) {
        if (distance > 0)
            fab.hide();//scroll up
        else
            fab.show();//scroll down

        downY = 0;
    }
}