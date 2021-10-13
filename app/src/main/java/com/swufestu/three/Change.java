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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Change extends AppCompatActivity{

    public static final String TAG = "activity_change";
    public static final int oneday = 1000 * 60 * 60 * 24;
    EditText RMB;
    TextView s;
    float dollarRate, euroRate, wonRate;
    Handler handler;
    String basetime;


    /**
     * 当此activity在其他activity中被调用时，则不会首先执行onCreate方法，
     * 当此activity直接被执行时则会首先执行onCreate方法
     * 因此当此activity在其他activity中被调用时要注意一些全局变量是否被初始化，否则会报错
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取系统当前时间
        Calendar calendar = Calendar.getInstance();
        String now = formatter.format(calendar.getTime());
        Log.i(TAG, "onCreate: nowtime=" + now);
        //从文件中获取上一次更新的时间
        SharedPreferences sp = getSharedPreferences("rate_file", Activity.MODE_PRIVATE);
        String basetimesp = sp.getString("base_time", "2000-01-01 00:00:00");

        //计算上一更新时间与当前时间的时间差
        Date nowdate = null;
        Date basedate = null;
        try {
            nowdate = formatter.parse(now);
            basedate = formatter.parse(basetimesp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long nd = nowdate.getTime();
        long bd = basedate.getTime();
        float delta_days = (nd - bd) / oneday;
        //时间差小于一天则重新获取汇率并更新文件
        if (delta_days > 0) {
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            basetime = formatter1.format(calendar.getTime());
            basetime = basetime + " 00:00:00";
            handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    Log.i(TAG, "handleMessage: 收到消息");
                    if (msg.what == 6) {
                        Bundle bdl = msg.getData();
                        dollarRate = 100 / bdl.getFloat("dollar", 0.0f);
                        euroRate = 100 / bdl.getFloat("euro", 0.0f);
                        wonRate = 100 / bdl.getFloat("won", 0.0f);
                        //将获取到的汇率以及需要更新的日期写入到rate_file.xml中
                        saveRateToSP();
                    }
                    super.handleMessage(msg);
                }
            };
            //启动线程
            GetRate gr = new GetRate();
            gr.setHandler(handler);
            Thread thread = new Thread(gr);
            thread.start();//this.run()
        }

        //从rate_file.xml中获取汇率
        getRateFromSP();
        Log.i(TAG, "onCreate: dollarRate=" + dollarRate);
        Log.i(TAG, "onCreate: euroRate=" + euroRate);
        Log.i(TAG, "onCreate: wonRate=" + wonRate);
    }

    private void saveRateToSP() {
        SharedPreferences sp = getSharedPreferences("rate_file", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //保存数据到rate_file.xml
        editor.putFloat("dollarRate", dollarRate);
        editor.putFloat("euroRate", euroRate);
        editor.putFloat("wonRate", wonRate);
        editor.putString("baseDate", basetime);

        editor.apply();
    }


    public void getRateFromSP() {
        SharedPreferences sp = getSharedPreferences("rate_file", Activity.MODE_PRIVATE);
        dollarRate = sp.getFloat("dollarRate", 0.0f);
        euroRate = sp.getFloat("euroRate", 0.0f);
        wonRate = sp.getFloat("wonRate", 0.0f);

        Log.i(TAG, "getRateFromSP: dollarRate=" + dollarRate);
        Log.i(TAG, "getRateFromSP: euroRate=" + euroRate);
        Log.i(TAG, "getRateFromSP: wonRate=" + wonRate);
    }

    public void ClickC(View view) {
        Log.i(TAG, "ClickC: ");

        RMB = findViewById(R.id.RMB);
        s = findViewById(R.id.money);
        int id = view.getId();
        if (RMB.getText().toString().equals("")) {
            s.setText("输入不能为空！！！");
        } else {
            float rmb = Float.parseFloat(RMB.getText().toString().trim());
            float result;
            if (id == R.id.dollar) {
                result = rmb * dollarRate;
                s.setText(String.valueOf(result) + "美元");
            } else if (id == R.id.euro) {
                result = rmb * euroRate;
                s.setText(String.valueOf(result) + "欧元");
            } else if (id == R.id.won) {
                result = rmb * wonRate;
                s.setText(String.valueOf(result) + "韩元");
            }
        }
    }

    public void toConfig(View view) {
        openConfig();
    }

    //菜单按钮响应
    private void openConfig() {
        Intent config = new Intent(this, Change_dest.class);
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);
        startActivityForResult(config, 1);
    }


    //将菜单项与Activity绑定在一起
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    //处理菜单按键
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_set) {
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2) {
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar", 0.1f);
            euroRate = bundle.getFloat("key_euro", 0.1f);
            wonRate = bundle.getFloat("key_won", 0.1f);

            Log.i(TAG, "onActivityResult: dollarRate=" + dollarRate);
            Log.i(TAG, "onActivityResult: euroRate=" + euroRate);
            Log.i(TAG, "onActivityResult: wonRate=" + wonRate);


        }
    }
}


