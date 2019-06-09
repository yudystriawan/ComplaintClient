package com.example.complaintclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintclient.api.ComplaintService;
import com.example.complaintclient.models.Comment;
import com.example.complaintclient.models.Complaint;
import com.example.complaintclient.network.ServiceGenerator;
import com.example.complaintclient.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCommentActivity extends AppCompatActivity {

    private ComplaintService service;
    private static Integer id;
    private EditText EditTextBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Tambah Tindak Lanjut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        service = ServiceGenerator.createService(ComplaintService.class);

        id = getIntent().getExtras().getInt("id");

        getComplaintData(id);

        ImageButton send = findViewById(R.id.img_btn_send_comment);
        EditTextBody = findViewById(R.id.text_comment_body);

        send.setOnClickListener(v -> {
            String body = EditTextBody.getText().toString();
            if (!body.isEmpty()) {
                sendComment(body, id);
            } else {
                Toast.makeText(getBaseContext(), "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getComplaintData(Integer id) {
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
        TextView topic = findViewById(R.id.text_c_topic);
        TextView body = findViewById(R.id.text_c_body);

        topic.setText(complaint.getTopic());
        body.setText(complaint.getBody());
    }

    private void sendComment(String body, Integer id) {

        Comment comment = new Comment(
                body
        );

        Call<String> call = service.createComment(comment, id);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
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

    private void show(String responseString) {
        Toast.makeText(this, responseString, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DetailsComplaintActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
