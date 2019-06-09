package com.example.complaintclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.complaintclient.api.ComplaintService;
import com.example.complaintclient.models.Complaint;
import com.example.complaintclient.network.ServiceGenerator;
import com.example.complaintclient.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateComplaintActivity extends AppCompatActivity {

    private ComplaintService service;
    private TextInputLayout textInputTopic;
    private Spinner spinnerCategory;
    private EditText editTextBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_complaint);

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        String token = sessionManager.getToken();
        service = ServiceGenerator.createService(ComplaintService.class, token);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Buat Pengaduan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        textInputTopic = findViewById(R.id.create_complaint_topic);
        spinnerCategory = findViewById(R.id.create_complaint_category);
        editTextBody = findViewById(R.id.create_complaint_body);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_complaint, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send) {
            sendComplaint();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendComplaint() {
        String topic = textInputTopic.getEditText().getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String body = editTextBody.getText().toString();

        if (body.isEmpty() || topic.isEmpty()) {
            Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {

            Complaint complaint = new Complaint(
                    topic,
                    body,
                    category
            );

            Call<String> call = service.createComplaint(complaint);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        String responseString = response.body();
                        show(responseString);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(CreateComplaintActivity.class.getSimpleName(), t.getLocalizedMessage(), t);
                }
            });
        }
    }

    private void show(String responseString) {
        Toast.makeText(this, responseString, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
