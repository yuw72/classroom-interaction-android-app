package com.horizon.myapp;

import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;
    private CheckBox checkBoxProfessor;
    private CheckBox checkBoxStudent;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(MainActivity.this);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!= null) {
            finish();
            startActivity(new Intent(getApplicationContext(), professorActivity.class));
        }
        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewSignin =  (TextView)findViewById(R.id.textViewSignin);
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

        checkBoxProfessor = (CheckBox)findViewById(R.id.checkBoxProfessor);
        checkBoxStudent = (CheckBox)findViewById(R.id.checkBoxStudent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void  registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if(!checkBoxStudent.isChecked() && !checkBoxProfessor.isChecked()) {
            Toast.makeText(this,"please check select your role",Toast.LENGTH_SHORT).show();
            return;
        }
        if(checkBoxStudent.isChecked() && checkBoxProfessor.isChecked()) {
            Toast.makeText(this,"please check select one role only",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this,"please enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        //validation
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }else{


                            Toast.makeText(MainActivity.this, "Failed Registration: ", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    private void storeUser() {
        String role = "1";
        if(!checkBoxStudent.isChecked() && !checkBoxProfessor.isChecked()) {
            //Toast.makeText(this,"please check select your role",Toast.LENGTH_SHORT).show();
            ;
        }
        else if(checkBoxStudent.isChecked() && checkBoxProfessor.isChecked()) {
            //Toast.makeText(this,"please check select one role only",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(checkBoxStudent.isChecked() && !checkBoxProfessor.isChecked()) {
            //Toast.makeText(this,"please check select one role only",Toast.LENGTH_SHORT).show();
            role = "2";
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String email = editTextEmail.getText().toString().trim();
        String username = separateEmail(email);
        try {
            myRef.child("roles").child(username).setValue(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String separateEmail(String email){
            String[] stringList = email.split("@");
            return stringList[0];
    }
    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            storeUser();
            registerUser();
        }
        if(view==textViewSignin){
            //will open sign in activity here
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
           // startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
    }


}
