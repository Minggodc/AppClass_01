package com.swufestu.three;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends ArrayAdapter {
    public MyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HashMap<String, String>> list) {
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent,
                    false);
        }
        Map<String,String> map = (Map<String,String>) getItem(position);
        TextView name = (TextView) itemView.findViewById(R.id.name);
        TextView rate = (TextView) itemView.findViewById(R.id.rate);

        name.setText(map.get("name"));
        rate.setText(map.get("rate"));

        return itemView;
    }
}
