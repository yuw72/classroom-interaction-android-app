package com.horizon.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnswerPostActivity extends AppCompatActivity {
    private TextView textViewTest;
    private TextView textViewPostList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String position = intent.getStringExtra("position");

        FloatingActionButton fab = findViewById(R.id.floatingActionPostBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //startActivity(new Intent(getApplicationContext(), professorPostActivity.class));
            }
        });

        textViewTest = (TextView)findViewById(R.id.textViewTest);
        textViewPostList = (TextView)findViewById(R.id.textViewPostList);
        textViewTest.setText(position);

        final DatabaseReference myRef  = FirebaseDatabase.getInstance().getReference("Classes");
    }

}
