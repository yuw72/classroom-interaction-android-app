package com.horizon.myapp;

import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class professorPostActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonProfessorSignout;
    private Button buttonProfessorPost;
    private Button buttonProfessorRecord;
    private Button buttonProfessorViewHistory;
    private TextView textViewProfessorClassName;
    private TextView textViewProfessorAttendance;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private RecyclerView mQuestionList;

    private final Context context = this;
    //final Intent intent=new Intent(context, AnswerPostActivity.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String className = intent.getStringExtra("result");
        firebaseAuth = FirebaseAuth.getInstance();

        FloatingActionButton backButton = findViewById(R.id.floatingActionProfessorButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), professorActivity.class));
            }
        });

        buttonProfessorSignout = (Button)findViewById(R.id.buttonProfessorSignout);
        buttonProfessorPost = (Button)findViewById(R.id.buttonProfessorPost);
        buttonProfessorRecord = ( Button)findViewById(R.id.buttonProfessorRecord);
        buttonProfessorViewHistory = (Button)findViewById(R.id.buttonProfessorViewHistory);
        textViewProfessorAttendance = (TextView)findViewById(R.id.textViewProfessorAttendance);
        textViewProfessorClassName = (TextView)findViewById(R.id.textViewProfessorClassName);

        buttonProfessorSignout.setOnClickListener(this);
        buttonProfessorPost.setOnClickListener(this);
        buttonProfessorRecord.setOnClickListener(this);
        buttonProfessorViewHistory.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("Questions");

        mQuestionList = (RecyclerView)findViewById(R.id.Professor_question_list);
        mQuestionList.setHasFixedSize(true);
        mQuestionList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<QuestionList,ProfessorQuestionListViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<QuestionList, ProfessorQuestionListViewHolder>(
                QuestionList.class,
                R.layout.post,
                ProfessorQuestionListViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(ProfessorQuestionListViewHolder viewHolder, QuestionList model, int position) {
                viewHolder.setQuestion(model.getQuestion());
            }
        };

        mQuestionList.setAdapter(firebaseRecyclerAdapter);
    }



    @Override
    public void onClick(View view) {
        if(view == buttonProfessorSignout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if(view == buttonProfessorViewHistory){

        }
        else if(view == buttonProfessorRecord) {

        }
        else if(view == buttonProfessorPost){

        }
    }
}
