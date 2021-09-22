package com.swufestu.three;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity{

    TextView b,s;
    EditText h,w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h = findViewById(R.id.height);
        w = findViewById(R.id.weight);
        Log.i("tag", "onCreate: "+"1234");
    }


    public void myClick(View view) {
        Log.i("tag", "onClick:AAAAAAAAAAAAAAAA ");
        b = findViewById(R.id.BMI);
        s = findViewById(R.id.suggestion);
        if(h.getText().toString().equals("")||w.getText().toString().equals("")){
            b.setText("输入不能为空！！！");
            s.setText("");
        }else{
            float height = Float.parseFloat(h.getText().toString().trim());
            float weight = Float.parseFloat(w.getText().toString().trim());
            float BMI = weight/(height*height);
            String suggestion;
            if(BMI<20){
                suggestion = "过瘦";
            }else if(BMI>=20&&BMI<=25){
                suggestion = "正常";
            }else if(BMI>25&&BMI<=30){
                suggestion = "超重";
            }else{
                suggestion = "肥胖";
            }
            b.setText("BMI为："+new DecimalFormat("0.00").format(BMI));
            s.setText("健康建议为："+suggestion);
        }
    }

    public void clickA(View view) {
    }
}