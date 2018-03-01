package com.example.shoaib.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.shoaib.user.listeners.ServiceCallBack;
import com.example.shoaib.user.utils.AppConstants;
import com.example.shoaib.user.utils.VolleyRequestHandler;
import org.json.JSONException;
import org.json.JSONObject;
public class signup extends Fragment implements View.OnClickListener, ServiceCallBack {
    EditText email;
    EditText password;
    EditText username;

    private AwesomeValidation awesomeValidation;
    public  static String email_st,password_st,username_st;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_signup2, container, false);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        Button btnPost = (Button) v.findViewById(R.id.button);
        btnPost.setOnClickListener(this);
        username = (EditText) v.findViewById(R.id.editText);
        password = (EditText) v.findViewById(R.id.editText4);
        email = (EditText) v.findViewById(R.id.editText3);

        TextView textView = (TextView) v.findViewById(R.id.textView);



        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                signin fragment = new signin();
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                awesomeValidation.addValidation(username, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", "Enter valid username");
                awesomeValidation.addValidation(email, Patterns.EMAIL_ADDRESS, "Enter valid email");
                awesomeValidation.addValidation(password, "((?=.*\\d)(?=.*[a-zA-Z]).{6,20})", "Enter valid password");
                if (awesomeValidation.validate()) {
                    try {
                        email_st = email.getText().toString();
                        password_st = password.getText().toString();
                        username_st = username.getText().toString();
                        JSONObject requestObject = new JSONObject();
                        requestObject.put("email", email_st);
                        requestObject.put("username", username_st);
                        requestObject.put("password", password_st);
                        VolleyRequestHandler postRequestHandler = new VolleyRequestHandler();
                        postRequestHandler.makeJsonRequest(getActivity(), Request.Method.POST, AppConstants.signup_URL, AppConstants.POST_TAG, this, requestObject);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        signin fragment = new signin();
                        fragmentTransaction.replace(R.id.fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                    break;

        }
    }

    @Override
    public void onSuccess(String requestTag, Object data) {
        Toast.makeText(getActivity(), requestTag + ">>>" + data, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(String requestTag, String message) {
        Toast.makeText(getActivity(), requestTag + ">>>" + message, Toast.LENGTH_LONG).show();
    }
}


