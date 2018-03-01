package com.example.shoaib.user.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shoaib.user.AppController;
import com.example.shoaib.user.listeners.ServiceCallBack;


import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyRequestHandler {

    private static final String TAG = "VolleyRequestHandler";

    public void makeJsonRequest(Context context, int methodType, String url, final String requestTag, final ServiceCallBack listener, JSONObject requestObject) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
       pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(methodType,
                url, requestObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            listener.onSuccess(requestTag, response);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                listener.onFailure(requestTag, error.getMessage());

               pDialog.hide();
            }


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, requestTag);

    }





}
