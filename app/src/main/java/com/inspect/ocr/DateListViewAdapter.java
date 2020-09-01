package com.inspect.ocr;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DateListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, String>> list;
    Context context;
    public static final String FIRST_COLUMN = "First";

    public DateListViewAdapter(Context context, ArrayList<HashMap<String, String>> list){
        super();
        this.context = context;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder{
        TextView txtFirst;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DateListViewAdapter.ViewHolder holder;



        if(convertView == null){

//            convertView = View.inflate( context, R.layout.inspect_list_row_body, null );
            convertView = LayoutInflater.from(context).inflate(R.layout.inspect_date_row, parent, false);
//            convertView.setLayoutParams( new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT) );
            holder=new DateListViewAdapter.ViewHolder();

            holder.txtFirst = (TextView) convertView.findViewById(R.id.inspect_date_txt);
            convertView.setTag(holder);
        }else{
            holder=(DateListViewAdapter.ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map=list.get(position);
        holder.txtFirst.setText(map.get(FIRST_COLUMN));
        return convertView;
    }
}
