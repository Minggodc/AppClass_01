package com.swufestu.three;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Change_dest extends AppCompatActivity {
    public static final String TAG = "activity_change_dest";
    EditText dollarText,euroText,wonText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dest);

        Intent intent = getIntent();
        float dollar = intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro = intent.getFloatExtra("euro_rate_key",0.0f);
        float won = intent.getFloatExtra("won_rate_key",0.0f);

        Log.i(TAG, "onCreate: dollar="+dollar);
        Log.i(TAG, "onCreate: euro="+euro);
        Log.i(TAG, "onCreate: won="+won);

        dollarText = findViewById(R.id.dollarRate);
        euroText = findViewById(R.id.euroRate);
        wonText = findViewById(R.id.wonRate);

        dollarText.setText(String.valueOf(dollar));
        euroText.setText(String.valueOf(euro));
        wonText.setText(String.valueOf(won));
    }

    public void save(View view){
        Log.i(TAG, "save: ");
        SharedPreferences sp = getSharedPreferences("rate_file", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //获取新的数据
        float newDollar = Float.parseFloat((dollarText.getText().toString()));
        float newEuro = Float.parseFloat((euroText.getText().toString()));
        float newWon = Float.parseFloat((wonText.getText().toString()));

        //保存数据到rate_file.xml
        editor.putFloat("dollarRate",newDollar);
        editor.putFloat("euroRate",newEuro);
        editor.putFloat("wonRate",newWon);
        editor.apply();


        Log.i(TAG, "save: 获取到新的值");
        Log.i(TAG, "onCreate: newDollar="+newDollar);
        Log.i(TAG, "onCreate: newEuro="+newEuro);
        Log.i(TAG, "onCreate: newWon="+newWon);


        //保存到Bundle或放入到Extra
        //Bundle
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);
        setResult(2,intent);
        //返回到调用界面
        finish();
    }
}