package com.inspect.ocr.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inspect.ocr.R;
import com.inspect.ocr.adapters.ServerImageAdapter;
import com.inspect.ocr.common.Common;
import com.inspect.ocr.common.LocalStorageManager;
import com.inspect.ocr.db.DbHelper;
import com.inspect.ocr.db.Queries;
import com.inspect.ocr.models.PatientImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.inspect.ocr.common.Common.currentActivity;
import static com.inspect.ocr.common.Common.dateFormatter;
import com.inspect.ocr.Global;
/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentServerList extends Fragment implements View.OnScrollChangeListener {


    private View root_view, parent_view;
    private TextView fromDateTxt, toDateTxt;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_refresh;
    private ServerImageAdapter mAdapter;
    private List<PatientImage> receiptImages = new ArrayList<>();

    private DbHelper dbHelper;
    private Queries query;

    private boolean loadingFlag = false;

    private Calendar fromDate, toDate;
    private ViewPager mViewPager;
    public FragmentServerList(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_fragment_server_list, null);

        swipe_refresh = root_view.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh.setColorSchemeResources(R.color.colorOrange, R.color.colorGreen, R.color.colorBlue, R.color.colorRed);
        recyclerView = root_view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnScrollChangeListener(this);

        //set data and list adapter
        mAdapter = new ServerImageAdapter(getActivity(), receiptImages);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new ServerImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, PatientImage obj, int position) {
                obj.setChecked(!obj.getChecked());
                mAdapter.notifyDataSetChanged();
            }
        });

        // on swipe list
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //getReciptImages(0);
                swipeProgress( false );
            }
        });

        toDate = Calendar.getInstance();
        fromDate = Calendar.getInstance();
        fromDate.setTimeInMillis(toDate.getTimeInMillis()-3600 * 24 * 1000L);
        toDateTxt = root_view.findViewById(R.id.to_date_value);
        toDateTxt.setText(dateFormatter.format(toDate.getTime()));
        final DatePickerDialog toDatePickerDialog = new DatePickerDialog(currentActivity, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                toDate.set(year, monthOfYear, dayOfMonth);
                toDateTxt.setText(dateFormatter.format(toDate.getTime()));
            }

        }, toDate.get(Calendar.YEAR), toDate.get(Calendar.MONTH), toDate.get(Calendar.DAY_OF_MONTH));

        toDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        fromDateTxt = root_view.findViewById(R.id.from_date_value);
        fromDateTxt.setText(dateFormatter.format(fromDate.getTime()));
        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(currentActivity, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fromDate.set(year, monthOfYear, dayOfMonth);
                fromDateTxt.setText(Common.getInstance().getDateByTimeZone( fromDate ) );
            }

        }, fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH), fromDate.get(Calendar.DAY_OF_MONTH));

        fromDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        RelativeLayout searchBtn = root_view.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReciptImages(0);
            }
        });

        RelativeLayout downloadBtn = root_view.findViewById(R.id.download_btn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadReceiptImages();
            }
        });

        RelativeLayout deleteBtn = root_view.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteReceiptImage();
                confirm();
            }
        });
        dbHelper = new DbHelper(currentActivity);
        query = new Queries(null, dbHelper);

        swipeProgress(false);
        getReciptImages(0);

        return root_view;
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

    private void confirm(){
        String imgIds = "";
        String imgPaths = "";
        final ArrayList<PatientImage> idList = new ArrayList<>();
        for (PatientImage item : receiptImages) {
            if (item.getChecked()) {
                idList.add(item);
                imgIds = imgIds + item.getImageId() + ",";
                String origin = item.getImagePath();
                String[] arr = origin.split("api");
                imgPaths = imgPaths + ".." + arr[1] + ",";
            }
        }
        if (imgIds.length() <= 0){
            Global.Companion.makeLargeTextToast( this.getContext(), "削除する画像を選択してください。" ).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext() );

        builder.setTitle( R.string.app_name ).setMessage( "選択された画像を削除しますか？");

        builder.setPositiveButton("はい", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                deleteReceiptImage();
            }
        });

        builder.setNegativeButton("いいえ", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteReceiptImage() {
        String imgIds = "";
        String imgPaths = "";
        final ArrayList<PatientImage> idList = new ArrayList<>();
        for (PatientImage item : receiptImages) {
            if (item.getChecked()) {
                idList.add(item);
                imgIds = imgIds + item.getImageId() + ",";
                String origin = item.getImagePath();
                String[] arr = origin.split("api");
                imgPaths = imgPaths + ".." + arr[1] + ",";
            }
        }
        if (imgIds.length() <= 0)
            return;

        imgIds = imgIds.substring(0, imgIds.length()-1);
        imgPaths = imgPaths.substring(0, imgPaths.length()-1);

        swipeProgress(true);
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("image_ids", imgIds)
                .add("image_paths", imgPaths)
                .build();
        final Request request = new Request.Builder()
                .url( Global.DELETE_IMAGE_URL )
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeProgress(false);
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    final String responseStr = response.body().string();
                    JSONObject object = new JSONObject(responseStr);
                    String status = (String) object.getJSONObject("status").get("status_code");
                    if (status.equals("-1")) { //success
                        for (PatientImage item : idList) {
                            query.deleteImage(item.getImageId());
                        }
                    } else {//error
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeProgress(false);
                        getReciptImages(0);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        swipeProgress(false);
    }

    public void getReciptImages(final int page) {
        swipeProgress(true);
        LocalStorageManager localStorageManager = new LocalStorageManager();
        OkHttpClient client = new OkHttpClient();

        Date fDate = new Date( (fromDate.get( Calendar.YEAR)-1900), fromDate.get( Calendar.MONTH), fromDate.get( Calendar.DATE) );
        String fDateStr = fDate.toString();
        Date tDate = new Date( (toDate.get( Calendar.YEAR)-1900), toDate.get( Calendar.MONTH), toDate.get( Calendar.DATE) );
        String toDateStr = tDate.toString();


        RequestBody formBody = new FormBody.Builder()
                .add("user_id", localStorageManager.getUserLoginStatus())
                .add("from_date", fDateStr )
                .add("to_date", toDateStr )
                .add("page", String.valueOf(page))
                .build();
        final Request request = new Request.Builder()
                .url( Global.RECEIPT_IMAGES_URL)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeProgress(false);
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    final String responseStr = response.body().string();
                    JSONObject object = new JSONObject(responseStr);
                    String status = (String) object.getJSONObject("status").get("status_code");
                    if(page == 0){
                        if( receiptImages != null ){
                            receiptImages.clear();
                        }
                    }
                    if (status.equals("-1")) { //success
                        JSONArray dataList = object.getJSONArray("images");
                        for (int i=0; i<dataList.length(); i++) {
                            JSONObject data = (JSONObject) dataList.get(i);
                            PatientImage imgData = new PatientImage();
                            imgData.setImageId(data.getInt("image_id"));
                            imgData.setThumbImagePath( data.getString( "thumb_image_path"));
                            imgData.setHospitalId(data.getString("hospital_id"));
                            imgData.setPatientId(data.getString("patient_id"));
                            imgData.setUserId(data.getString("user_id"));
                            imgData.setImagePath(data.getString("image_path"));
                            imgData.setReceiptDate(data.getDouble("receipt_date"));
                            imgData.setCreatedDate(data.getDouble("created_date"));
                            receiptImages.add(imgData);
                        }
                        if (dataList.length()>0)
                            loadingFlag = false;
                    } else {
                        //error
                        String empty = "";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        swipeProgress(false);
                    }
                });
            }
        });
    }

    private void downloadReceiptImages() {
        for (PatientImage item : receiptImages) {
            if (item.getChecked()) {
                query.insertImage(item);
                item.setChecked(false);
            }
        }
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        View view = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        if( view == null ) return;
        int diff = view.getBottom() - recyclerView.getHeight() + scrollY;
/*        if (diff == 0) {
            if (!loadingFlag) {
                getReciptImages(receiptImages.size());
                loadingFlag = true;
            }
        }*/
    }
}
