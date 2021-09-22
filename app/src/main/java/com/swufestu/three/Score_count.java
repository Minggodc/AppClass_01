package com.swufestu.three;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Score_count extends AppCompatActivity {
    public static final String TAG = "MainActivity3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }
    public void myClick(View view) {
        Log.i(TAG, "myClick: ");
       TextView one = findViewById(R.id.point);
       int points = Integer.parseInt(one.getText().toString().trim());
       if(view.getId()==R.id.p1){
           points+=1;
       }else if(view.getId()==R.id.p2){
           points+=2;
       }else if(view.getId()==R.id.p3){
           points+=3;
       }else{
           points=0;
       }
       one.setText(String.valueOf(points));

    }
}