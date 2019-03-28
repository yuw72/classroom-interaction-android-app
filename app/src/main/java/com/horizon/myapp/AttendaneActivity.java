package com.horizon.myapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import com.firebase.ui.database.FirebaseListAdapter;

public class AttendaneActivity extends AppCompatActivity{
    private ListView listView;
    List<String> userList = new ArrayList<>();
    List<Object> countList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendane);
        FloatingActionButton returnBack = findViewById(R.id.floatingActionPostBack);
        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //startActivity(new Intent(getApplicationContext(), professorPostActivity.class));
            }
        });

        Intent intent = getIntent();

        listView = (ListView) findViewById(R.id.listViewAttend);

        final String user = globalVar.user;
        final String className = intent.getStringExtra("result");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Classes");
        final DatabaseReference attendanceRef = rootRef.child(className).child("Attendance");
        Log.d("class name is ", className);

        attendanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        userList.add(ds.getKey());
                        countList.add(ds.getValue());
                }
                CustomListAdapter adapter = new CustomListAdapter(AttendaneActivity.this, userList,countList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });


    }


}
