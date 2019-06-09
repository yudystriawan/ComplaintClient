package com.example.complaintclient.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.complaintclient.R;
import com.example.complaintclient.models.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.row_comment, viewGroup, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder commentViewHolder, int i) {
        commentViewHolder.body.setText(comments.get(i).getBody());

        commentViewHolder.instanceName.setText(comments.get(i).getUser().getInstance().getName());

    }

    @Override
    public int getItemCount() {

        return (comments != null) ? comments.size() : 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView instanceName, body;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            instanceName = itemView.findViewById(R.id.details_comment_instance_name);
            body = itemView.findViewById(R.id.details_comment_body);
        }
    }
}
