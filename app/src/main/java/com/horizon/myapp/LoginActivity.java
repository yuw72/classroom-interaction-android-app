package com.horizon.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSignin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private int userRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
//        if(firebaseAuth.getCurrentUser() != null) {
//            //get profile activity
//            finish();
//            startActivity(new Intent(getApplicationContext(), studentActivity.class));
//        }
        progressDialog = new ProgressDialog(this);

        buttonSignin = (Button) findViewById(R.id.buttonSignin);

        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewSignup =  (TextView)findViewById(R.id.textViewSignup);

        buttonSignin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);


    }

    private String separateEmail(String email){
        String[] stringList = email.split("@");
        return stringList[0];
    }

    private void userLogin() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        //validation
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            String username = separateEmail(email);
                            userRole(username);
                        }
                    }
                });
    }

    private void userRole(String username) {
        final String user = username;
        DatabaseReference myRef  = FirebaseDatabase.getInstance().getReference("roles").child(username);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String role=dataSnapshot.getValue(String.class);
                int cmp_role = Integer.parseInt(role);
                Log.d("role value is", "cmp_role is" + cmp_role);
                if(cmp_role == 1) {
                    //start profile activity
                    globalVar.user = user;
                    Log.i("global user is ", globalVar.user);
                    finish();
                    startActivity(new Intent(getApplicationContext(), professorActivity.class));
                }
                else{
                    globalVar.user = user;
                    finish();
                    startActivity(new Intent(getApplicationContext(),studentActivity.class));

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
        if(view == buttonSignin) {
            userLogin();
        }
        else if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
