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

public class studentActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextClass;
    private Button buttonJoin;
    private Button buttonSignout;
    private String result;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextClass = (EditText)findViewById(R.id.editTextClass);
        buttonJoin = (Button)findViewById(R.id.buttonJoin);
        buttonSignout = (Button)findViewById(R.id.buttonSignout);

        buttonJoin.setOnClickListener(this);
        buttonSignout.setOnClickListener(this);

    }

    private void joinClass() {
        final String className = editTextClass.getText().toString().trim();
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
                    Intent classIntent = new Intent(getApplicationContext(),studentPostActivity.class);
                    classIntent.putExtra("result",className);
                    globalVar.className = className;
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
    @Override
    public void onClick(View view) {
        if(view == buttonJoin)
        {
            joinClass();
        }
        else if(view == buttonSignout)
        {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


}
