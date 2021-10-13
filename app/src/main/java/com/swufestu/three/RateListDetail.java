package com.swufestu.three;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.internal.StringUtil;

public class RateListDetail extends AppCompatActivity implements TextWatcher {


    TextView currency,result;
    EditText RMB;
    String rateStr;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list_detail);

        //从RateListActivity获取数据
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        rateStr = intent.getStringExtra("rate");

        currency = findViewById(R.id.currency);
        result = findViewById(R.id.Result);
        RMB = findViewById(R.id.RMB);
        currency.setText(name);

        //初始化监听
        RMB.addTextChangedListener(this);;
    }

    /**
     * 重写TextWatcher接口下的方法
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    //写在onTextChanged方法中实现实时计算并显示汇率
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //处理获取到的汇率为空的情况
        if(rateStr.isEmpty()){
            result.setText("未获取到汇率");
        }else{
            //处理输入框中的数据被删完的情况
            if(!RMB.getText().toString().isEmpty()){
                float rmb = Float.parseFloat(RMB.getText().toString().trim());
                float rate = Float.parseFloat(rateStr);
                float re = (float) (100.0/rate)*rmb;
                result.setText(String.valueOf(re)+name);
            }else{
                result.setText("");
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void Return(View view){
        //返回上一个界面
        finish();
    }
}