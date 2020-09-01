package com.inspect.ocr.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.inspect.ocr.R;
import com.inspect.ocr.ActivityDetail;
import com.inspect.ocr.adapters.LocalImageAdapter;
import com.inspect.ocr.db.DbHelper;
import com.inspect.ocr.db.Queries;
import com.inspect.ocr.models.PatientImage;

import java.util.ArrayList;
import java.util.List;

import static com.inspect.ocr.common.Common.currentActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLocalList extends Fragment {

    private View root_view, parent_view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_refresh;
    private LocalImageAdapter mAdapter;
    private List<PatientImage> receiptImages = new ArrayList<>();

    private DbHelper dbHelper;
    private Queries query;
    private ViewPager mViewPager;

    public FragmentLocalList(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_fragment_local_list, null);

        swipe_refresh = root_view.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh.setColorSchemeResources(R.color.colorOrange, R.color.colorGreen, R.color.colorBlue, R.color.colorRed);
        recyclerView = root_view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new LocalImageAdapter(getActivity(), receiptImages);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new LocalImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, PatientImage obj, int position) {
                Intent intent = new Intent(currentActivity, ActivityDetail.class);
                intent.putExtra("data", obj);
                startActivity(intent);
            }
        });

        // on swipe list
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReciptImagesFromLocal();
            }
        });

        dbHelper = new DbHelper(currentActivity);
        query = new Queries(null, dbHelper);

        swipeProgress(false);
        getReciptImagesFromLocal();

        return root_view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
            getReciptImagesFromLocal();
    }


    private void swipeProgress(final boolean show) {
        if (!show) {
            swipe_refresh.setRefreshing(show);
            return;
        }
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(show);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        swipeProgress(false);
    }

    public void getReciptImagesFromLocal() {
//        swipeProgress(true);
        receiptImages = query.getImages();
        mAdapter.setListData(receiptImages);
        swipeProgress(false);
    }
}
