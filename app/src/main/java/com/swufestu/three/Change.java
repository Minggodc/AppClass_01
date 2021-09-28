package com.swufestu.three;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Change extends AppCompatActivity {

    public static final String TAG = "activity_change";
    EditText RMB;
    TextView s;
    float dollarRate = 0.15f;
    float euroRate = 0.13f;
    float wonRate = 182.39f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        RMB = findViewById(R.id.RMB);
        s = findViewById(R.id.money);


    }
    public void ClickC(View view){
        Log.i(TAG, "ClickC: ");

        int id = view.getId();
        if(RMB.getText().toString().equals("")) {
            s.setText("输入不能为空！！！");
        }else{
            float rmb = Float.parseFloat(RMB.getText().toString().trim());
            float result;
            if(id==R.id.dollar){
                result = rmb*dollarRate;
                s.setText(String.valueOf(result)+"美元");
            }else if(id==R.id.euro){
                result = rmb*euroRate;
                s.setText(String.valueOf(result)+"欧元");
            }else if(id==R.id.won){
                result = rmb*wonRate;
                s.setText(String.valueOf(result)+"韩元");
            }
        }
    }
    public void toConfig(View view){
        openConfig();
    }

    private void openConfig(){
        Intent config = new Intent(this,Change_dest.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i(TAG, "toConfig: dollarRate="+dollarRate);
        Log.i(TAG, "toConfig: euroRate="+euroRate);
        Log.i(TAG, "toConfig: wonRate="+wonRate);

        startActivityForResult(config,1);
    }


    //将菜单项与Activity绑定在一起
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }
    //处理菜单按键
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&resultCode==2){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);

            Log.i(TAG, "onActivityResult: dollarRate="+dollarRate);
            Log.i(TAG, "onActivityResult: euroRate="+euroRate);
            Log.i(TAG, "onActivityResult: wonRate="+wonRate);


        }
    }
}
