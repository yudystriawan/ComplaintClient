package com.example.complaintclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.complaintclient.utils.SessionManager;

public class SettingFragment extends Fragment {

    private SessionManager sessionManager;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        CardView action = rootView.findViewById(R.id.card_setting_action);
        TextView text = rootView.findViewById(R.id.text_action);

        sessionManager = new SessionManager(rootView.getContext());

        if (sessionManager.getToken() != null) {
            text.setText("Logout");
            action.setOnClickListener(v -> {
                sessionManager.destroy();
            });
        } else {
            text.setText("LoginActivity");
            action.setOnClickListener(v -> {
                Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                startActivity(intent);
            });
        }


        return rootView;
    }
}
