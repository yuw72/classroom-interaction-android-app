package com.horizon.myapp;

import android.arch.core.executor.DefaultTaskExecutor;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class studentPostActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonSignout;
    private Button buttonPost;
    private Button buttonAttendance;
    private TextView textViewClassName;
    private EditText editTextAttendPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private RecyclerView mQuestionList;
    private String className;
    private String user = globalVar.user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        className = intent.getStringExtra("result");
        firebaseAuth = FirebaseAuth.getInstance();

        FloatingActionButton backButton = findViewById(R.id.floatingActionButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), studentActivity.class));
            }
        });

        mQuestionList = (RecyclerView)findViewById(R.id.question_list);
        mQuestionList.setHasFixedSize(true);
        mQuestionList.setLayoutManager(new LinearLayoutManager(this));

        buttonAttendance = (Button)findViewById(R.id.buttonAttendance);
        buttonPost = (Button)findViewById(R.id.buttonPost);
        buttonSignout = (Button)findViewById(R.id.buttonSignout);
        textViewClassName = (TextView)findViewById(R.id.textViewClassName);
        editTextAttendPassword = (EditText)findViewById(R.id.editTextAttendPassword);
        buttonAttendance.setOnClickListener(this);
        buttonPost.setOnClickListener(this);
        buttonSignout.setOnClickListener(this);
        textViewClassName.setText(className);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("Questions");
    }

    @Override
    protected void onStart() {
        super.onStart();

       FirebaseRecyclerAdapter<QuestionList,QuestionListViewHolder2> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<QuestionList, QuestionListViewHolder2>(
               QuestionList.class,
               R.layout.post,
               QuestionListViewHolder2.class,
               mDatabase

       ) {
           @Override
           protected void populateViewHolder(QuestionListViewHolder2 viewHolder, QuestionList model, int position) {
                viewHolder.setQuestion(model.getQuestion());
           }
       };

       mQuestionList.setAdapter(firebaseRecyclerAdapter);
    }



    public static class QuestionListViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public QuestionListViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setQuestion(String question){
            TextView show_question = (TextView) mView.findViewById(R.id.textViewQuestion);
            show_question.setText(question);
        }


    }

    private void add_attendance(String className){
        final String attendanceCode = editTextAttendPassword.getText().toString().trim();

        if ( TextUtils.isEmpty(attendanceCode)) {
            Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Classes").child(className);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long isOpen = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals("Record")){
                        isOpen = (long)ds.getValue();
                        Log.d("is open = ", String.valueOf(isOpen));
                        break;
                    }

                }
                if(isOpen==1){
                    check_code(attendanceCode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void check_code(final String code){
        final DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("AttendanceCode");
        myDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String passwrd = dataSnapshot.getValue(String.class);
                if(passwrd.equals(code)){
                    attend();
                }
                else{
                    Toast.makeText(studentPostActivity.this, "Wrong Attendance Code, Please enter again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void attend(){
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("Attendance");;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int is_Found = 0;
                long curr_cnt = 0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    if(childSnapshot.getKey().equals(user)){
                        curr_cnt = (long) childSnapshot.getValue();
                        is_Found = 1;
                        break;
                    }
                }

                if(is_Found == 1){
                    //change to new count = old_count++;
                    curr_cnt++;
                    Log.d("crr count is ", String.valueOf(curr_cnt));
                    myRef.child(user).setValue(curr_cnt);
                }
                else{
                    Log.d("is found",String.valueOf(is_Found));
                    myRef.child(user).setValue(1);
                }
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });
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
            add_attendance(className);
        }
        else if(view == buttonPost)
        {
            Intent classIntent = new Intent(getApplicationContext(), postCommentsActivity.class);
            globalVar.className = className;
            startActivity(classIntent);
        }
    }
}
