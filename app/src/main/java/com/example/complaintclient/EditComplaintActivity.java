package com.example.complaintclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintclient.api.ComplaintService;
import com.example.complaintclient.models.Complaint;
import com.example.complaintclient.models.Instance;
import com.example.complaintclient.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditComplaintActivity extends AppCompatActivity {

    private ComplaintService service;
    private Spinner spinnerInstance;
    private Integer id;
    private TextView textViewTopic, textViewName, textViewCategory, textViewBody, textViewCreatedAt;
    private CheckBox checkBoxNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_complaint);

        service = ServiceGenerator.createService(ComplaintService.class);

        textViewTopic = findViewById(R.id.text_edit_topic);
        textViewName = findViewById(R.id.text_edit_name);
        textViewCategory = findViewById(R.id.text_edit_category);
        textViewBody = findViewById(R.id.text_edit_body);
        checkBoxNegative = findViewById(R.id.checkBox_negative);
        textViewCreatedAt = findViewById(R.id.text_edit_createdAt);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Pengaduan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id = getIntent().getExtras().getInt("id");

        getInstances();

        ImageButton save = findViewById(R.id.img_btn_edit_save);
        save.setOnClickListener(v -> saveUpdate(id));

    }

    private void saveUpdate(final Integer id) {

        String instance = spinnerInstance.getSelectedItem().toString();
        Boolean negative = checkBoxNegative.isChecked();

        Complaint complaint = new Complaint(
                negative,
                instance
        );

        Call<String> call = service.editComplaint(complaint, id);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), response.body(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), DetailsComplaintActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(EditComplaintActivity.class.getSimpleName(), t.getLocalizedMessage(), t);
            }
        });
    }

    private void getInstances() {
        Call<List<Instance>> call = service.getInstances();

        call.enqueue(new Callback<List<Instance>>() {
            @Override
            public void onResponse(Call<List<Instance>> call, Response<List<Instance>> response) {
                if (response.isSuccessful()) {
                    List<Instance> instances = response.body();
                    generateInstanceList(instances);
                    getComplaint(id);
                }
            }

            @Override
            public void onFailure(Call<List<Instance>> call, Throwable t) {

            }
        });
    }

    private void generateInstanceList(List<Instance> instances) {
        List<String> instanceName = new ArrayList<>();

        for (int i = 0; i < instances.size(); i++) {
            instanceName.add(instances.get(i).getName());
        }

        spinnerInstance = findViewById(R.id.spinner_edit_instance);

        spinnerInstance.setAdapter(
                new ArrayAdapter<>(EditComplaintActivity.this, android.R.layout.simple_spinner_dropdown_item, instanceName)
        );


    }

    private void getComplaint(Integer id) {
        Call<Complaint> call = service.getComplaint(id);

        call.enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Complaint complaint = response.body();

                if (complaint != null) {
                    generateComplaint(complaint);
                }
            }

            @Override
            public void onFailure(Call<Complaint> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateComplaint(Complaint complaint) {

        textViewTopic.setText(complaint.getTopic());
        textViewName.setText(complaint.getUser().getName());
        textViewCategory.setText(complaint.getCategory());
        textViewBody.setText(complaint.getBody());
        checkBoxNegative.setChecked(complaint.isNegative());
        textViewCreatedAt.setText(complaint.getCreated_at().toString());

        ArrayAdapter newAdapter = (ArrayAdapter) spinnerInstance.getAdapter();
        if (!newAdapter.isEmpty()) {
            int spinnerPosition = newAdapter.getPosition(complaint.getInstance().getName());
            spinnerInstance.setSelection(spinnerPosition);
        }
    }
}
