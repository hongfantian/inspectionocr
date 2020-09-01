package com.inspect.ocr;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.inspect.ocr.data.ResultList;

import androidx.viewpager.widget.PagerAdapter;

public class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ListPageView curView = null;
    private ListPageView beforeView = null;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ListPageView page = new ListPageView( mContext, position);
//        page.setLayoutParams( new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT) );
        collection.addView( page );
        page.setTag(position);
        if( position == 0 && Global.Companion.getPAGEVIER_FIRST_LOAD() == 0 ){
            Global.Companion.setPAGEVIER_FIRST_LOAD(1);
            setCurrentView( page );
        }
        return page;
    }
    public void setCurrentView( ListPageView  _view){
        curView = _view;
    }
    public ListPageView getCurrentView(){
        return curView;
    }
    public ListPageView getBeforeView(){
        return beforeView;
    }

    @Override
    public int getCount() {
        return (int) Math.ceil( (double) ResultList.Companion.getCount()/Global.VIEWCOUNT );
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}
