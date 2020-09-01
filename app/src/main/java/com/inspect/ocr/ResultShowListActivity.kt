package com.inspect.ocr

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager


class ResultShowListActivity : AppCompatActivity(){
    var curTop:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_show_list)
        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        viewPager.adapter = CustomPagerAdapter(this)

        Global.PAGEVIER_FIRST_LOAD = 0
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
           override fun onPageScrollStateChanged(state: Int) {
                var i:Int = 1
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                var customAdapter:CustomPagerAdapter = viewPager.adapter as CustomPagerAdapter
                var bListPageView:ListPageView? = customAdapter.currentView
                var cListPageView:ListPageView? = viewPager.findViewWithTag(position) as ListPageView
                if( cListPageView != null ){
                    if( bListPageView != null ){
                        var top:Int =  bListPageView.listView.firstVisiblePosition
                        cListPageView.listView.setSelection( top )
                    }
                    customAdapter.setCurrentView( cListPageView )
                }
/*
                var v1 = viewPager.getChildAt( position )
                var customAdapter:CustomPagerAdapter = viewPager.adapter as CustomPagerAdapter
                var cListPageView:ListPageView? = null
                if( v1 != null ){
                    cListPageView = v1 as ListPageView
                    if( cListPageView != null ){
                        var listView1:ListView = cListPageView.listView as ListView
                        var bListPageView:ListPageView? = customAdapter.beforeView
                        if( bListPageView != null ){
                            listView1.setSelection( bListPageView.listView.firstVisiblePosition )
                        }
                        customAdapter.setCurrentView( cListPageView )
                    }
                }else{
                    cListPageView = customAdapter.currentView
                    var bListPageView:ListPageView? = customAdapter.beforeView
                    if( cListPageView != null ){
                        if( bListPageView != null ){
                            cListPageView.listView.setSelection( bListPageView.listView.firstVisiblePosition )
                        }
                    }
                }
*/
            }
        })
    }
}