package com.example.shoaib.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.example.shoaib.user.listeners.ServiceCallBack;
import com.example.shoaib.user.utils.AppConstants;
import com.example.shoaib.user.utils.RowItem;
import com.example.shoaib.user.utils.VolleyRequestHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.example.shoaib.user.utils.AppConstants.loginemail;


public class MyListFragment extends Fragment implements
        AdapterView.OnItemClickListener,View.OnClickListener, ServiceCallBack {
    ListView listView;
    List<RowItem> rowItems;
    public static  String[] titles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.list_fragment, container, false);
        listView = (ListView) v.findViewById(R.id.list);
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("email", loginemail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestHandler postRequestHandler = new VolleyRequestHandler();
        postRequestHandler.makeJsonRequest(getActivity(), Request.Method.POST, AppConstants.getcamera_URL, AppConstants.POST_TAG, this, requestObject);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "Item " + (position + 1) + ": " + rowItems.get(position),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        AppConstants.connectemail=titles[position];
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        livefragment fragment = new livefragment();
        fragmentTransaction.add(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSuccess(String requestTag, Object data) throws UnsupportedEncodingException {
        try {
            titles=null;

            Log.d("Json",  data.toString());
            JSONObject mainObj  = new JSONObject(data.toString());
            if(mainObj != null){
                JSONArray list = mainObj.getJSONArray("Users");
                titles=new String[list.length()];

                if(list != null){
                    for(int i = 0; i < list.length();i++){
                        JSONObject elem = list.getJSONObject(i);
                        if(elem != null){
                            titles[i] = elem.getString("name");


                        }
                    }
                }
            }
            for(int i=0;i<titles.length;i++)
            {
                Log.d("Json",titles[i]);
            }

            rowItems = new ArrayList<RowItem>();
            for (int i = 0; i < titles.length; i++) {
                RowItem item = new RowItem(titles[i]);
                rowItems.add(item);
            }


            CustomListViewAdapter adapter = new CustomListViewAdapter(getActivity(),
                    R.layout.list_item, rowItems);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String requestTag, String message) {

    }
}
