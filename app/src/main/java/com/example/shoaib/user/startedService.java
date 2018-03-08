package com.example.shoaib.user;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.shoaib.user.utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.shoaib.user.utils.AppConstants.ip_address;
import static com.example.shoaib.user.utils.AppConstants.loginemail;

/**
 * Created by Shoaib on 3/3/2018.
 */

public class startedService extends Service {
     String SIGNALING_URI =ip_address;
     Intent i;
    Socket socket;
    private static final String USERNOTI = "usernoti";
    String email;
    JSONObject obj;
    @Override
    public void onCreate() {


        super.onCreate();
        Log.d("done","Created");
        SharedPreferences prefs = getSharedPreferences("pref", MODE_PRIVATE);

            loginemail = prefs.getString("email", "None");//"No name defined" is the default value.

        try {
            socket = IO.socket(SIGNALING_URI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        obj = new JSONObject();
        try {
            obj.put("email",loginemail);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("done","Started");
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        socket.connect();
        socket.emit(USERNOTI,obj);
       socket.on("notification", new Emitter.Listener() {

            @Override
            public void call(Object... args) {


                JSONObject obj = (JSONObject) args[0];
                try {
                    String email=obj.getString("email");

                String loginemail=obj.getString("loginemail");
                AppConstants.connectemail=loginemail;
                Log.d("done",email+loginemail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.close();
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }

        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
