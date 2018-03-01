package com.example.shoaib.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import static com.example.shoaib.user.utils.AppConstants.connectemail;

public class CmailFragment extends Fragment {
    Button button;
    EditText email;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cmail, container, false);
        button = (Button) v.findViewById(R.id.button);
        email = (EditText) v.findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectemail = email.getText().toString();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                livefragment fragment = new livefragment();
                fragmentTransaction.add(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            });

       return v;


    }
}