package com.horizon.myapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class commentListViewHolder extends RecyclerView.ViewHolder {
    View mView;
    public commentListViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

    }

    public void setComment(String comment){
        TextView show_comment = (TextView) mView.findViewById(R.id.textViewCommentList);
        show_comment.setText(comment);
    }
}
