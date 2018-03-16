package com.example.shoaib.user.notification;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shoaib.user.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<Notification> moviesList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView detection;

        public MyViewHolder(View view) {
            super(view);
            detection = (TextView) view.findViewById(R.id.detection);


        }
    }


    public NotificationAdapter(List<Notification> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notification movie = moviesList.get(position);
        holder.detection.setText(movie.getDetection());


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}