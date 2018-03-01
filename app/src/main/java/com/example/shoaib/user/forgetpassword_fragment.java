package com.example.shoaib.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.shoaib.user.listeners.ServiceCallBack;
import com.example.shoaib.user.utils.AppConstants;
import com.example.shoaib.user.utils.VolleyRequestHandler;

import org.json.JSONException;
import org.json.JSONObject;


public class forgetpassword_fragment extends Fragment implements View.OnClickListener, ServiceCallBack {
    EditText email;
    public  static String email_st;
    TextView text;
    Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgetpassword, container, false);

        button = (Button) v.findViewById(R.id.button);
         text = (EditText) v.findViewById(R.id.editText4);
        button.setOnClickListener(this);
        email = (EditText) v.findViewById(R.id.editText);


        return v;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                try {
                    email_st = email.getText().toString();

                    JSONObject requestObject = new JSONObject();
                    requestObject.put("email", email_st);
                    VolleyRequestHandler postRequestHandler = new VolleyRequestHandler();
                    postRequestHandler.makeJsonRequest(getActivity(), Request.Method.POST, AppConstants.sendmail_URL, AppConstants.POST_TAG,this, requestObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onSuccess(String requestTag, Object data) {
        Toast.makeText(getActivity(), requestTag + ">>>" + data, Toast.LENGTH_LONG).show();
        try {
            JSONObject jsonObject = new JSONObject(data.toString());
            boolean message = jsonObject.getBoolean("Message");
            boolean error = jsonObject.getBoolean("Error");
            Log.d("shoaib", String.valueOf(message));
            if(message==true) {
                text.setVisibility(View.VISIBLE);

            }
            if(error==true) {
                Toast.makeText(getActivity(), requestTag + ">>>" + "Please enter correct email and password", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(String requestTag, String message) {
        Toast.makeText(getActivity(), requestTag + ">>>" + message, Toast.LENGTH_LONG).show();

    }
}
