package com.example.complaintclient.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.complaintclient.DetailsComplaintActivity;
import com.example.complaintclient.R;
import com.example.complaintclient.models.Complaint;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {

    private List<Complaint> complaints;

    private HashMap<String, String> userDetails;

    public ComplaintAdapter(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.row_complaint, viewGroup, false);

        final ComplaintViewHolder viewHolder = new ComplaintViewHolder(view);
        viewHolder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(viewGroup.getContext(), DetailsComplaintActivity.class);
            intent.putExtra("id", complaints.get(viewHolder.getAdapterPosition()).getId());
            viewGroup.getContext().startActivity(intent);
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder complaintViewHolder, int i) {
        String date = new SimpleDateFormat("EEE dd-MMM-yy").format(complaints.get(i).getCreated_at());
        String role = complaints.get(i).getUser().getRole().getName();
        complaintViewHolder.topic.setText(complaints.get(i).getTopic());
        complaintViewHolder.body.setText(complaints.get(i).getBody());
        complaintViewHolder.created_at.setText(date);
        complaintViewHolder.name.setText(complaints.get(i).getUser().getName());
        if (complaints.get(i).isNegative()){
            complaintViewHolder.negative.setText("Penyimpangan");
        }else {
            complaintViewHolder.negative.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (complaints != null) ? complaints.size() : 0;
    }


    class ComplaintViewHolder extends RecyclerView.ViewHolder {

        private TextView topic, body, created_at, name, negative;
        private CardView cardView;

        public ComplaintViewHolder(View view) {
            super(view);
            topic = view.findViewById(R.id.text_complaintTopic);
            body = view.findViewById(R.id.text_complaintBody);
            created_at = view.findViewById(R.id.text_complaintCreatedAt);
            name = view.findViewById(R.id.text_userName);
            cardView = view.findViewById(R.id.card_view_complaint);
            negative = view.findViewById(R.id.text_status_negative);
        }
    }
}
