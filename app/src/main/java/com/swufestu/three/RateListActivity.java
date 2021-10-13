package com.swufestu.three;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RateListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String TAG = "RateListActivity";
    Handler handler;
    ArrayList<String> name,rate;
    ArrayList<String> info = new ArrayList<>();
    ListView mylist2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list);

        ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
        mylist2 = findViewById(R.id.mylist2);
        mylist2.setOnItemClickListener(this);

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
                        HashMap<String,String> map = new HashMap<String, String>();
                        map.put("name",name.get(i));
                        map.put("rate",rate.get(i));
                        listItems.add(map);
                    }
                    //不适用自定义列表布局
//                    SimpleAdapter listItemAdapter = new SimpleAdapter(RateListActivity.this,
//                            listItems,
//                            R.layout.list_item,
//                            new String[]{"name","rate"},
//                            new int[]{R.id.name,R.id.rate});
//                    ListView mylist2 = findViewById(R.id.mylist2);
//                    mylist2.setAdapter(listItemAdapter);
                    //自定义列表布局
                    MyAdapter ma = new MyAdapter(RateListActivity.this,R.layout.list_item,listItems);

                    mylist2.setAdapter(ma);

                }
                super.handleMessage(msg);
            }
        };
        //启动线程
        GetRateTwo grt = new GetRateTwo();
        grt.setHandler(handler);
        Thread thread = new Thread(grt);
        thread.start();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Object itemAtPosition = mylist2.getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
        String name = map.get("name");
        String rate = map.get("rate");
        Log.i(TAG, "onItemClick: titleStr="+name);
        Log.i(TAG, "onItemClick: detailStr="+rate);

        //向RateListDetail传数据
        Intent config = new Intent(this, RateListDetail.class);
        config.putExtra("name", name);
        config.putExtra("rate", rate);
        startActivityForResult(config, 1);


//        TextView title = (TextView) view.findViewById(R.id.name);
//        TextView detail = (TextView) view.findViewById(R.id.rate);
//        String title2 = String.valueOf(title.getText());
//        String detail2 = String.valueOf(detail.getText());
//        Log.i(TAG, "onItemClick: title2="+title2);
//        Log.i(TAG, "onItemClick: detail2="+detail2);
    }
}