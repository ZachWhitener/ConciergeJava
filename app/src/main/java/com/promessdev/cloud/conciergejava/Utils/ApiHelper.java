package com.promessdev.cloud.conciergejava.Utils;

import android.content.Context;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by zach on 9/20/2017.
 */

public class ApiHelper {

    private Context mContext;
    private static final String TAG = "ApiHelper";
    private Boolean mIsConnected = false;
    /**
     * Constructor
     * Pass in the context when instantiating ApiHelper
     * @param context
     */
    ApiHelper(Context context) {
        this.mContext = context;
    }

    /**
    public boolean IsConnected() {

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this.mContext);
        String url = "http://172.25.63.1/myconnect/";
        Boolean isConnected = false;
        // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, Request.Method.GET, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return mIsConnected;
    }
    **/



}
