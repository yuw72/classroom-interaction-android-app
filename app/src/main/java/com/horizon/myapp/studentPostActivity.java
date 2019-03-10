package com.horizon.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class studentPostActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonSignout;
    private Button buttonPost;
    private Button buttonAttendance;
    private TextView textViewClassName;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private RecyclerView mQuestionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String className = intent.getStringExtra("result");
        firebaseAuth = FirebaseAuth.getInstance();

        FloatingActionButton backButton = findViewById(R.id.floatingActionButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), studentActivity.class));
            }
        });

        mQuestionList = (RecyclerView)findViewById(R.id.question_list);
        mQuestionList.setHasFixedSize(true);
        mQuestionList.setLayoutManager(new LinearLayoutManager(this));

        buttonAttendance = (Button)findViewById(R.id.buttonAttendance);
        buttonPost = (Button)findViewById(R.id.buttonPost);
        buttonSignout = (Button)findViewById(R.id.buttonSignout);
        textViewClassName = (TextView)findViewById(R.id.textViewClassName);

        buttonAttendance.setOnClickListener(this);
        buttonPost.setOnClickListener(this);
        buttonSignout.setOnClickListener(this);
        textViewClassName.setText(className);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("Questions");
    }

    @Override
    protected void onStart() {
        super.onStart();

       FirebaseRecyclerAdapter<QuestionList,QuestionListViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<QuestionList, QuestionListViewHolder>(
               QuestionList.class,
               R.layout.post,
               QuestionListViewHolder.class,
               mDatabase

       ) {
           @Override
           protected void populateViewHolder(QuestionListViewHolder viewHolder, QuestionList model, int position) {
                viewHolder.setQuestion(model.getQuestion());
           }
       };

       mQuestionList.setAdapter(firebaseRecyclerAdapter);
    }



    public static class QuestionListViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public QuestionListViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setQuestion(String question){
            TextView show_question = (TextView) mView.findViewById(R.id.textViewQuestion);
            show_question.setText(question);
        }


    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignout)
        {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if(view == buttonAttendance){

        }
        else if(view == buttonPost)
        {

        }
    }
}
