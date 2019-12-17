package com.horizon.myapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class QuestionListViewHolder extends RecyclerView.ViewHolder {

    View mView;
    Context mContext;
    public QuestionListViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void setQuestion(String question){
        TextView show_question = (TextView) mView.findViewById(R.id.textViewQuestion);
        show_question.setText(question);
        Button comment = (Button)mView.findViewById(R.id.buttonComment);

        final int position= getLayoutPosition();
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AnswerPostActivity.class);
                intent.putExtra("position",String.valueOf(position));
                mContext.startActivity(intent);
            }

        });
    }


}
