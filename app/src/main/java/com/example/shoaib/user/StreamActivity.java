package com.example.shoaib.user;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.shoaib.user.cloud.cloudActivity;
import com.example.shoaib.user.cloud.viewActivity;
import com.example.shoaib.user.listeners.ServiceCallBack;
import com.example.shoaib.user.notification.notificationFragment;
import com.example.shoaib.user.utils.AppConstants;
import com.example.shoaib.user.utils.VolleyRequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.example.shoaib.user.utils.AppConstants.loginemail;

public class StreamActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener, ServiceCallBack {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                       // CmailFragment fragment = new CmailFragment();
                                       MyListFragment fragment = new MyListFragment();
                                       fragmentTransaction.add(R.id.content_frame, fragment);

                                       fragmentTransaction.commit();
                                   }
                               });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.textView);
        navUsername.setText(loginemail);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // CmailFragment fragment = new CmailFragment();
        MyListFragment fragment = new MyListFragment();
        fragmentTransaction.add(R.id.content_frame, fragment);

        fragmentTransaction.commit();


    }
public void drive(IntentSender intent, int REQUEST_CODE_OPENER)
{
    try {
        startIntentSenderForResult(

                intent, REQUEST_CODE_OPENER, null, 0, 0, 0);
    } catch (IntentSender.SendIntentException e) {
        e.printStackTrace();
    }

}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
          /*  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // CmailFragment fragment = new CmailFragment();
            cloudFragment fragment = new cloudFragment();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit()*/;

            Intent intent = new Intent(this, cloudActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_gallery) {

          /*  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // CmailFragment fragment = new CmailFragment();
            viewFragment fragment = new viewFragment();
            fragmentTransaction.replace(R.id.content_frame, fragment);

            fragmentTransaction.commit();*/
            Intent intent = new Intent(this, viewActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();

        }
        else if (id == R.id.nav_noti) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // CmailFragment fragment = new CmailFragment();
            notificationFragment fragment = new notificationFragment();
            fragmentTransaction.replace(R.id.content_frame, fragment);

            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSuccess(String requestTag, Object data) throws UnsupportedEncodingException {
        Toast.makeText(this, requestTag + ">>>" + data, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(String requestTag, String message) {
        Toast.makeText(this, requestTag + ">>>" + message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("pref", MODE_PRIVATE).edit();
        editor.putString("email", loginemail);
        editor.apply();
        Intent i= new Intent(getApplicationContext(), startedService.class);
        getApplicationContext().startService(i);
        getApplicationContext().stopService(i);
    }
}
