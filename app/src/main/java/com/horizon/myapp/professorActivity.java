package com.horizon.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class professorActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextProfessorClass;
    private Button buttonProfessorJoin;
    private Button buttonProfessorSignout;
    private Button buttonProfessorCreate;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        editTextProfessorClass = (EditText)findViewById(R.id.editTextProfessorClass);
        buttonProfessorJoin = (Button)findViewById(R.id.buttonProfessorJoin);
        buttonProfessorCreate = (Button)findViewById(R.id.buttonProfessorCreate);
        buttonProfessorSignout = (Button)findViewById(R.id.buttonProfessorSignout);

        buttonProfessorJoin.setOnClickListener(this);
        buttonProfessorSignout.setOnClickListener(this);
        buttonProfessorCreate.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == buttonProfessorJoin){
            joinClass();
        }
        else if(view == buttonProfessorCreate){
            createClass();
        }
        else if(view == buttonProfessorSignout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void joinClass() {
        final String className = editTextProfessorClass.getText().toString().trim();
        if (TextUtils.isEmpty(className)) {
            Toast.makeText(this, "please enter class name", Toast.LENGTH_SHORT).show();
            return;
        }
        final DatabaseReference myRef  = FirebaseDatabase.getInstance().getReference("Classes");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(className).exists())
                {
                    Intent classIntent = new Intent(getApplicationContext(),professorPostActivity.class);
                    classIntent.putExtra("result",className);
                    finish();
                    startActivity(classIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "no class is found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void createClass() {
        final String className = editTextProfessorClass.getText().toString().trim();
        if (TextUtils.isEmpty(className)) {
            Toast.makeText(this, "please enter class name", Toast.LENGTH_SHORT).show();
            return;
        }
        final DatabaseReference myRef  = FirebaseDatabase.getInstance().getReference("Classes");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(className).exists()) {
                    Toast.makeText(getApplicationContext(), "class already exists", Toast.LENGTH_SHORT).show();
                }
                else{
                    add_database(className);
                    Intent classIntent = new Intent(getApplicationContext(),professorPostActivity.class);
                    classIntent.putExtra("result",className);
                    finish();
                    startActivity(classIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void add_database(String className) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Classes");

        try {
            mDatabase.child(className).child("id").setValue(1);
            mDatabase.child(className).child("id").setValue("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
