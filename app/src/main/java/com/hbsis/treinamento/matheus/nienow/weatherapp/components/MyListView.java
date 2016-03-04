package com.hbsis.treinamento.matheus.nienow.weatherapp.components;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by matheus.nienow on 01/12/2015.
 */
public class MyListView extends ListView {
    private float downY = 0;
    private boolean consumeTouchEvents = false;
    private FloatingActionButton fab;

    //Construtores
    public MyListView(Context context) {
        super(context);
    }
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //Set fab
    public void attachFabForScroll(FloatingActionButton fab) {
        this.fab = fab;
    }

    //Override
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

    //Esconde ou mostra fab
    private void handleScroll(float distance) {
        if (distance > 0)
            fab.hide();//scroll up
        else
            fab.show();//scroll down

        downY = 0;
    }
}