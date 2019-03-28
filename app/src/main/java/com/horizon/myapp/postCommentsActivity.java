package com.horizon.myapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class postCommentsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSubmit;
    private EditText editTextQuestion;
    private String className = globalVar.className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);
        buttonSubmit = (Button)findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);
        editTextQuestion = (EditText)findViewById(R.id.editTextQuestion);
        FloatingActionButton backButton = findViewById(R.id.floatingActionProfessorButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void submit_question(){
        String question = editTextQuestion.getText().toString().trim();
        if ( TextUtils.isEmpty(question)) {
            Toast.makeText(this, "Please enter your question", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("here ", "before db");
        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("Questions");
        myDatabase.push().child("question").setValue(question);

    }

    @Override
    public void onClick(View view) {
        if(view == buttonSubmit){
            Log.d("here " , "press submit");
            submit_question();
        }
    }
}
