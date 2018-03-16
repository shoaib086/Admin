package com.example.shoaib.user.notification;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.shoaib.user.R;
import com.example.shoaib.user.utils.AppConstants;

public class playvideoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_playvideo, container, false);
        VideoView simpleVideoView = (VideoView) v.findViewById(R.id.simpleVideoView);
       // simpleVideoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.wordpress));
         simpleVideoView.setVideoURI(Uri.parse("/storage/emulated/0/Recorded Videos/video"+ AppConstants.reqid+".mp4"));

        MediaController mediaController = new
                MediaController(getContext());
        mediaController.setAnchorView(simpleVideoView);
        simpleVideoView.setMediaController(mediaController);
        simpleVideoView.start();

        return v;
    }


}
