package com.example.complaintclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintclient.adapters.CommentAdapter;
import com.example.complaintclient.api.ComplaintService;
import com.example.complaintclient.models.Comment;
import com.example.complaintclient.models.Complaint;
import com.example.complaintclient.network.ServiceGenerator;
import com.example.complaintclient.utils.SessionManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsComplaintActivity extends AppCompatActivity {

    private ComplaintService service;
    private static Integer id;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_complaint);

        service = ServiceGenerator.createService(ComplaintService.class);


        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getSession();
        role = userDetails.get("ROLE");

        id = getIntent().getExtras().getInt("id");

        getComplaint(id);
        getComments(id);

        ImageButton edit = findViewById(R.id.img_btn_toEdit);
        ImageButton comment = findViewById(R.id.img_btn_toComment);



        if (role == null || role.equals("ROLE_USER")) {
            edit.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
        } else if (role.equals("ROLE_ADMIN_SUPVR")) {
            edit.setVisibility(View.GONE);
            comment.setVisibility(View.VISIBLE);
        }else if(role.equals("ROLE_ADMIN_APP")){
            edit.setVisibility(View.VISIBLE);
            comment.setVisibility(View.GONE);
        }

        edit.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), EditComplaintActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        comment.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), CreateCommentActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
    }

    private void getComments(Integer id) {
        Call<List<Comment>> call = service.getCommentsByComplaintId(id);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Comment> comments = response.body();

                generateComments(comments);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        TextView tv_name = findViewById(R.id.details_name);
        TextView tv_body = findViewById(R.id.details_body);
        TextView tv_created_at = findViewById(R.id.details_createdAt);
        TextView tv_instance = findViewById(R.id.details_instance);
        TextView tv_category = findViewById(R.id.text_edit_category);
        TextView tv_topic = findViewById(R.id.details_complaint_topic);
        TextView tv_percent = findViewById(R.id.details_percent);

        tv_name.setText(complaint.getUser().getName());
        tv_body.setText(complaint.getBody());
        tv_created_at.setText(complaint.getCreated_at().toString());
        tv_instance.setText(complaint.getInstance().getName());
        tv_topic.setText(complaint.getTopic());
        tv_category.setText(complaint.getCategory());

        double percent = complaint.getPercent();
        int parse = (int) (percent * 100);
        String acc = Integer.toString(parse);
        tv_percent.setText(acc + "%");

        if (role == null){
            tv_percent.setVisibility(View.GONE);
        }else if (!role.equals("ROLE_ADMIN_APP")){
            tv_percent.setVisibility(View.GONE);
        }else{
            tv_percent.setVisibility(View.VISIBLE);
        }
//        else if (!role.equals("ROLE_ADMIN_APP")){
//            tv_percent.setVisibility(View.GONE);
//        }else{
//            tv_percent.setVisibility(View.VISIBLE);
//        }


    }

    private void generateComments(List<Comment> comments) {
        RecyclerView recyclerView = findViewById(R.id.recycle_comments);
        CommentAdapter adapter = new CommentAdapter(comments);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailsComplaintActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
