package com.example.shoaib.user;

import android.Manifest;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;


public class MainActivity extends AppCompatActivity {
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hello();
        if(isServiceRunning()){
            Intent i= new Intent(getApplicationContext(), startedService.class);
            getApplicationContext().stopService(i);
        }

        FragmentManager fragmentManager =getFragmentManager();
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        signin fragment = new signin();
        fragmentTransaction.add(R.id.fragment, fragment);
       
        fragmentTransaction.commit();


    }
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.example.shoaib.user.startedService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    void hello(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.INTERNET},
                1);
    }
 /*   @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }
    */


}
