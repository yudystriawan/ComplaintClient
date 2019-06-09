package com.example.complaintclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.complaintclient.adapters.ComplaintAdapter;
import com.example.complaintclient.api.ComplaintService;
import com.example.complaintclient.models.Complaint;
import com.example.complaintclient.network.ServiceGenerator;
import com.example.complaintclient.utils.SessionManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ComplaintService service;
    private View rootView;
    private String role;
    private HashMap<String, String> userDetails;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        service = ServiceGenerator.createService(ComplaintService.class);
        SessionManager sessionManager = new SessionManager(rootView.getContext());




        FloatingActionButton toCreateComplaint = rootView.findViewById(R.id.flt_btn_toCreateComplaint);
        userDetails = sessionManager.getSession();
        role = userDetails.get("ROLE");

        getComplaints();

        if (role == null || role.equals("ROLE_USER")) {
            toCreateComplaint.setVisibility(View.VISIBLE);
        } else {
            toCreateComplaint.setVisibility(View.GONE);
        }

        toCreateComplaint.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateComplaintActivity.class);
            startActivity(intent);
        });

        return rootView;
    }

    private void getComplaints() {

        Call<List<Complaint>> call;

        if (role == null || role.equals("ROLE_USER") || role.equals("ROLE_ADMIN_APP")) {
            call = service.getComplaints();
        } else if (role.equals("ROLE_ADMIN_SUPVR")) {
            call = service.getComplaintsByNegative(true);
        } else {
            String instanceName = userDetails.get("INSTANCE");
            call = service.getComplaintsByInstance(instanceName);
        }

        call.enqueue(new Callback<List<Complaint>>() {
            @Override
            public void onResponse(Call<List<Complaint>> call, Response<List<Complaint>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Complaint> complaints = response.body();

                generateComplaintList(complaints);
            }

            @Override
            public void onFailure(Call<List<Complaint>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateComplaintList(List<Complaint> complaints) {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycle_view_complaints);
        ComplaintAdapter adapter = new ComplaintAdapter(complaints);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
