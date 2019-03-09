package com.horizon.myapp;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonSendData;
    private TextView textViewID;
    private TextView textViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        textViewResult = (TextView)findViewById(R.id.textViewResult);
        Intent intent = getIntent();
        String str = intent.getStringExtra("result");
        textViewResult.setText(str);


        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        textViewUserEmail = (TextView)findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome"+user.getEmail());

        buttonLogout = (Button)findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);

        textViewID = (TextView)findViewById(R.id.showID);

        buttonSendData = (Button)findViewById(R.id.sendData);
        buttonSendData.setOnClickListener(this);


        DatabaseReference myRef  = FirebaseDatabase.getInstance().getReference("Users/user/ID");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int id=dataSnapshot.getValue(Integer.class);
                textViewID.setText(String.valueOf(id));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == ProfileActivity.RESULT_OK){
                String result=data.getStringExtra("result");
                Log.d("result","result is "+result);
            }
            if (resultCode == ProfileActivity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.d("testing", "result is cancelled");
            }
        }
    }//onActivityResult

    private void write_to_database() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
//       // myRef.child("user").child("Name").setValue("Yuchao");
//        myRef.setValue("Hello, World!");
//        Toast.makeText(this, "data sent---", Toast.LENGTH_SHORT).show();
        try {
            myRef.child("Users").child("user").child("Name").setValue("Yuchao");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        if(view==buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if(view==buttonSendData)
        {
            write_to_database();
        }
    }


}
