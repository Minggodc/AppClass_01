package com.swufestu.three;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Change extends AppCompatActivity implements Runnable{

    public static final String TAG = "activity_change";
    EditText RMB;
    TextView s;
    float dollarRate,euroRate,wonRate;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        //从rate_file.xml中获取汇率
        getRateFromSP();

        RMB = findViewById(R.id.RMB);
        s = findViewById(R.id.money);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: 收到消息");
                if(msg.what==6){
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: getMessage msg="+str);
                    s.setText(str);
                }
                super.handleMessage(msg);
            }
        };
        //启动线程
        Thread thread = new Thread(this);
        thread.start();//this.run()
    }



    public void getRateFromSP() {
        SharedPreferences sp = getSharedPreferences("rate_file", Activity.MODE_PRIVATE);
        dollarRate = sp.getFloat("dollarRate",0.15f);
        euroRate = sp.getFloat("euroRate",0.13f);
        wonRate = sp.getFloat("wonRate",182.39f);

        Log.i(TAG, "onCreate: dollarRate="+dollarRate);
        Log.i(TAG, "onCreate: euroRate="+euroRate);
        Log.i(TAG, "onCreate: wonRate="+wonRate);
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

    @Override
    public void run() {
        Log.i(TAG, "run: run----------");
        //延迟
        try {
            Thread.sleep(3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        //获取网络数据
        URL url = null;
        try {
            url = new URL("https://www.swufe.edu.cn/info/1002/18045.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            Log.i(TAG, "run: msg="+inputStream2String(in));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //发送消息
        Message msg = handler.obtainMessage(6);
        //msg.what=6;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);
    }


    private String inputStream2String(InputStream inputStream) throws  IOException{
        final int buffersize = 1024;
        final char[] buffer = new char[buffersize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"utf-8");
        for(;;){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz<0){
                break;
            }
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}


