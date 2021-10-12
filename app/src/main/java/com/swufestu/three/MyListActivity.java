package com.swufestu.three;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity {

    private static String TAG = "MyListActivity";

    Handler handler;
    ArrayList<String> name,rate;
    ArrayList<String> info = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        ListView lv = findViewById(R.id.mylist);

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: MyListActivity收到消息");
                if (msg.what == 7) {
                    Bundle bdl = msg.getData();
                    name = bdl.getStringArrayList("name");
                    rate = bdl.getStringArrayList("rate");
                    int iter = name.size();
                    for (int i = 0; i<iter; i++){
                        info.add(name.get(i)+"==>"+rate.get(i));
                    }
                    //
                    ListAdapter  adapter = new ArrayAdapter<String>(MyListActivity.this,
                            android.R.layout.simple_list_item_1,info);
                    lv.setAdapter(adapter);
                    //setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
        //启动线程
//        GetRate gr = new GetRate();
//        gr.setHandler(handler);
//        Thread thread = new Thread(gr);
//        thread.start();//this.run()
        GetRateTwo grt = new GetRateTwo();
        grt.setHandler(handler);
        Thread thread = new Thread(grt);
        thread.start();


    }
}