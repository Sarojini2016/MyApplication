package com.justin.medical;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by upendra  on 7/2/16.
 */
public class BakersDelight extends Application {

    public static final String TAG = BakersDelight.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static BakersDelight mInstance;
    private static String MID;

    public boolean IsAddMoneyStatus = false;
    public boolean PaymentStatus = false;
    public String AddedAmount = "";
    public boolean IsActive = true;
    public boolean ShouldDisplayPasscode = false;
    public boolean ShouldUpdateUsername = false;
    Handler RedirectHandler = new Handler();
    Handler sessionHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized BakersDelight getInstance() {
        return mInstance;
    }



    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void StartTimerSession() {
        ShouldDisplayPasscode = false;
        sessionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (IsActive) {
                    sessionHandler.postDelayed(this, 1 * 60 * 1000);
                } else {
                    ShouldDisplayPasscode = true;
                }
            }
        }, 2 * 60 * 1000);
    }
}