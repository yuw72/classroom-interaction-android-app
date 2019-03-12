package com.horizon.myapp;

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

public class AnswerPostActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewTest;
    private TextView textViewPostList;
    private Button buttonPostComment;
    private EditText editTextPostComment;
    private RecyclerView mCommentList;
    private DatabaseReference mDatabase;
    private String className = globalVar.className;
    private String position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        position = intent.getStringExtra("position");

        FloatingActionButton returnBack = findViewById(R.id.floatingActionPostBack);
        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //startActivity(new Intent(getApplicationContext(), professorPostActivity.class));
            }
        });

        textViewTest = (TextView)findViewById(R.id.textViewTest);
        textViewPostList = (TextView)findViewById(R.id.textViewPostList);
        textViewTest.setText(position);
        readQuestion(position);

        editTextPostComment = (EditText)findViewById(R.id.editTextComment);


        buttonPostComment = (Button)findViewById(R.id.buttonPostComment);
        buttonPostComment.setOnClickListener(this);

        mCommentList = (RecyclerView)findViewById(R.id.Professor_comment_list);
        mCommentList.setHasFixedSize(true);
        mCommentList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child("Questions");

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count=0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    if(Integer.parseInt(position) == count){
                        String node = childSnapshot.getKey();
                        mDatabase=mDatabase.child(node).child("Comments");
                        listComment();
                        return;
                    }
                    else{
                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    private void listComment() {
        FirebaseRecyclerAdapter<CommentList,commentListViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<CommentList, commentListViewHolder>(
                CommentList.class,
                R.layout.comment_list,
                commentListViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(commentListViewHolder viewHolder, CommentList model, int position) {
                viewHolder.setComment(model.getComment());
            }
        };

        mCommentList.setAdapter(firebaseRecyclerAdapter);
    }

    private void readQuestion(String pos) {
        final String position = pos;
        final DatabaseReference myRef  = FirebaseDatabase.getInstance().getReference("Classes").child(className).child("Questions");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count=0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    if(Integer.parseInt(position) == count){
                        String question = childSnapshot.child("question").getValue(String.class);
                        textViewPostList.setText(question);
                        return;
                    }
                    else{
                        count++;
                    }
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
        if(view == buttonPostComment) {
            postComments();
        }

    }

    private void postComments() {
        final String postComment = editTextPostComment.getText().toString().trim();
        if(TextUtils.isEmpty(postComment)) {
            Toast.makeText(this,"please enter your comments",Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Classes").child(className).child("Questions");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count=0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){

                    if(Integer.parseInt(position) == count){
                        String node = childSnapshot.getKey();
                        myRef.child(node).child("Comments").push().child("comment").setValue(postComment);
                        return;
                    }
                    else{
                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
