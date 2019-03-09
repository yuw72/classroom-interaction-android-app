package com.horizon.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class studentPostActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonSignout;
    private Button buttonPost;
    private Button buttonAttendance;
    private TextView textViewClassName;
    private FirebaseAuth firebaseAuth;
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

        buttonAttendance = (Button)findViewById(R.id.buttonAttendance);
        buttonPost = (Button)findViewById(R.id.buttonPost);
        buttonSignout = (Button)findViewById(R.id.buttonSignout);
        textViewClassName = (TextView)findViewById(R.id.textViewClassName);

        buttonAttendance.setOnClickListener(this);
        buttonPost.setOnClickListener(this);
        buttonSignout.setOnClickListener(this);
        textViewClassName.setText(className);
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
