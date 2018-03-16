package com.example.shoaib.user.notification;


        import android.graphics.Movie;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.DividerItemDecoration;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.example.shoaib.user.R;
        import com.example.shoaib.user.forgetpassword_fragment;
        import com.example.shoaib.user.listeners.ServiceCallBack;
        import com.example.shoaib.user.utils.AppConstants;
        import com.example.shoaib.user.utils.VolleyRequestHandler;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.UnsupportedEncodingException;
        import java.util.ArrayList;
        import java.util.List;

        import static com.example.shoaib.user.utils.AppConstants.loginemail;

public class notificationFragment extends Fragment implements View.OnClickListener, ServiceCallBack{
    private List<Notification> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v= inflater.inflate(R.layout.fragment_notification, container, false);
        super.onCreate(savedInstanceState);


        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        mAdapter = new NotificationAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));



        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("name", loginemail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestHandler postRequestHandler = new VolleyRequestHandler();
        postRequestHandler.makeJsonRequest(getActivity(), Request.Method.POST, AppConstants.getnotification_URL, AppConstants.POST_TAG,this, requestObject);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Notification movie = movieList.get(position);
                AppConstants.reqid=movie.getVideoid();
                Toast.makeText(getContext().getApplicationContext(), movie.getVideoid() + " is selected!", Toast.LENGTH_SHORT).show();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                playvideoFragment fragment = new playvideoFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return v;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String requestTag, Object data) throws UnsupportedEncodingException {
        Toast.makeText(getActivity(), data.toString(), Toast.LENGTH_LONG).show();
        Log.d("tts", data.toString());
        JSONObject mainObj = null;
        try {
            mainObj = new JSONObject(data.toString());
            Notification movie;
            if (mainObj != null) {
                JSONArray list = mainObj.getJSONArray("Users");
                if (list != null) {
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject elem = list.getJSONObject(i);
                        if (elem != null) {
                            String [] date = elem.getString("datetime").split("T");
                            String [] time = date[1].split("\\.");
                            String mainChapterNumber = date[1].split("\\.", 2)[0];
                            Log.d("done",mainChapterNumber);
                            String detection="Activity has been detected on camera "+elem.getString("name")+" Faces has been detected "+elem.getString("faces")+" at "+date[0]+" "+mainChapterNumber;
                           // Notification movie = new Notification(elem.getString("activity"), elem.getString("faces"), elem.getString("datetime"),elem.getString("videoid"));
                            movie = new Notification(detection,elem.getString("videoid"));
                            movieList.add(movie);
                        }
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onFailure(String requestTag, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }


}