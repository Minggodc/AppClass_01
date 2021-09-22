package com.swufestu.three;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ABScore_count extends AppCompatActivity {
    public static final String TAG = "MainActivity4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
    }

    public void clickA(View viewa) {
        Log.i(TAG, "clickA: ");
        TextView A = findViewById(R.id.scoreA);
        int scoreA = Integer.parseInt(A.getText().toString().trim());
        if(viewa.getId()==R.id.Ap1){
            scoreA+=1;
        }else if(viewa.getId()==R.id.Ap2){
            scoreA+=2;
        }else if(viewa.getId()==R.id.Ap3){
            scoreA+=3;
        }
        A.setText(String.valueOf(scoreA));
    }

    public void clickB(View viewb) {
        Log.i(TAG, "clickB: ");
        TextView B = findViewById(R.id.scoreB);
        int scoreB = Integer.parseInt(B.getText().toString().trim());
        if(viewb.getId()==R.id.Bp1){
            scoreB+=1;
        }else if(viewb.getId()==R.id.Bp2){
            scoreB+=2;
        }else if(viewb.getId()==R.id.Bp3){
            scoreB+=3;
        }
        B.setText(String.valueOf(scoreB));
    }
    public void clickRe(View viewr) {
        TextView A = findViewById(R.id.scoreA);
        TextView B = findViewById(R.id.scoreB);
        A.setText("0");
        B.setText("0");
    }
}