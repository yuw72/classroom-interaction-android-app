package com.horizon.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class studentPostActivity extends AppCompatActivity {
    private TextView textViewTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_post);
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        textViewTest = findViewById(R.id.textViewTest);
        textViewTest.setText(result);
    }
}
