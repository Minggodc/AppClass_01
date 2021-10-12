package com.swufestu.three;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetRate implements Runnable {

    public static final String TAG = "GetRate";
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        Log.i(TAG, "run: run----------");

        //使用Jsoup获取网络数据
        //用一个map将获取到的币种汇率以<k,v>形式存储起来
        Map<String,String> map = new HashMap<String,String>();
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> rate = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("https://usd-cny.com/").get();
            Element firstTable = doc.getElementsByTag("table").first();
            Elements tds = firstTable.getElementsByTag("td");
            //Log.i(TAG, "run: tds="+tds);
            for(int i = 0;i<tds.size();i+=5){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+1);
                name.add(td1.text());
                rate.add(td2.text());
                map.put(td1.text(),td2.text());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        //发送消息给Change.java
        Message msg = handler.obtainMessage(6);
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar",Float.parseFloat(map.get("美元")));
        bdl.putFloat("euro",Float.parseFloat(map.get("欧元")));
        bdl.putFloat("won",Float.parseFloat(map.get("韩币")));
        msg.setData(bdl);
        handler.sendMessage(msg);

        //发送消息给MyListActivity.java
        Message msg2 = handler.obtainMessage(7);
        Bundle bdl2 = new Bundle();
        bdl2.putStringArrayList("name",name);
        bdl2.putStringArrayList("rate",rate);
        msg2.setData(bdl2);
        handler.sendMessage(msg2);
    }
}
