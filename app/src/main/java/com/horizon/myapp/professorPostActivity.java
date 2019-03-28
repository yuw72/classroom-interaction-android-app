package com.horizon.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class professorPostActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonProfessorSignout;
    private Button ButtonRecord;
    private Button buttonProfessorViewHistory;
    private TextView textViewProfessorClassName;
    private TextView textViewAttendCode;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private RecyclerView mQuestionList;

    private final Context context = this;
    private String className;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
         className = intent.getStringExtra("result");
        String user = globalVar.user;
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
        ButtonRecord = ( Button) findViewById(R.id.ButtonRecord);
        buttonProfessorViewHistory = (Button)findViewById(R.id.buttonProfessorViewHistory);
        textViewAttendCode = (TextView)findViewById(R.id.textViewAttendCode);
        textViewProfessorClassName = (TextView)findViewById(R.id.textViewProfessorClassName);

        textViewAttendCode.setText(className);
        buttonProfessorSignout.setOnClickListener(this);
        ButtonRecord.setOnClickListener(this);
        buttonProfessorViewHistory.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("Questions");

        mQuestionList = (RecyclerView)findViewById(R.id.Professor_question_list);
        mQuestionList.setHasFixedSize(true);
        mQuestionList.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("AttendanceCode");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String code = dataSnapshot.getValue(String.class);
                textViewAttendCode.setText(code);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });

        final DatabaseReference myDB = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("Record");
        myDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Integer.class) == 0){
                    ButtonRecord.setText("Record OFF");
                }
                else{
                    ButtonRecord.setText("Record ON");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<QuestionList, QuestionListViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<QuestionList, QuestionListViewHolder>(
                QuestionList.class,
                R.layout.post,
                QuestionListViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(QuestionListViewHolder viewHolder, QuestionList model, int position) {
                viewHolder.setQuestion(model.getQuestion());
            }
        };

        mQuestionList.setAdapter(firebaseRecyclerAdapter);
    }

    private void record_Attendance() {
        final DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("Record");
        myDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Integer.class) == 0){
                    myDatabase.setValue(1);
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("AttendanceCode");
                    String pass = generate_random();
                    db.setValue(pass);
//                    ButtonRecord.setText("Record ON");
                }
                else{
                    myDatabase.setValue(0);
//                    ButtonRecord.setText("Record OFF");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    private String generate_random(){
        int random = (int)(Math.random() * 9);
        int random2 = (int)(Math.random() * 9);
        int random3 = (int)(Math.random() * 9);
        int random4 = (int)(Math.random() * 9);
        return String.valueOf(random)+String.valueOf(random2)+String.valueOf(random3)+String.valueOf(random4);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonProfessorSignout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if(view == buttonProfessorViewHistory){
            Intent classIntent = new Intent(getApplicationContext(), AttendaneActivity.class);
            classIntent.putExtra("result", className);
            globalVar.className = className;
            //finish();
            startActivity(classIntent);
        }
        else if(view == ButtonRecord) {
            record_Attendance();
        }
    }

}
