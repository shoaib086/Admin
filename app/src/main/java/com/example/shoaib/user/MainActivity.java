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


        FragmentManager fragmentManager =getFragmentManager();
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        signin fragment = new signin();
        fragmentTransaction.replace(R.id.fragment, fragment);
       
        fragmentTransaction.commit();


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
