package com.inspect.ocr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import jp.dki.labtestvaluesgraph.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GraphActivity extends AppCompatActivity {

    private static final String TAG = GraphActivity.class.getSimpleName();

    private Integer mUserId;
    private String mItemName;
    private LineChart mLineChart;

    private Integer mNumItemVal;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_graph);

        Intent intent = getIntent();
        String itemName = intent.getStringExtra("item");
        String userId = intent.getStringExtra("userId");

        Log.i(TAG, "item:" + itemName);
        Log.i(TAG, "userId:" + userId);

        mItemName = itemName;
        mUserId = Integer.valueOf(userId);

        TextView textView = findViewById(R.id.text);
        String unit = "";
        if (Data.getUnit(mItemName) != null) {
            unit = " [" + Data.getUnit(mItemName) + "]";
        }
        textView.setText(mItemName + unit);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        initChart();

        new ShowChartTask().execute();
    }

    private void initChart() {
        mLineChart = findViewById(R.id.chart_StaticLineGraph);

        mLineChart.getDescription().setEnabled(false);

        mLineChart.setTouchEnabled(true);
        mLineChart.setDragEnabled(true);
        mLineChart.setGridBackgroundColor( Color.parseColor("#FF000000") );

        YAxis leftYAxis = mLineChart.getAxisLeft();
        leftYAxis.setGridColor( Color.parseColor("#FF000000"));
        leftYAxis.setGridLineWidth( 1.0f );
        leftYAxis.setTextSize(24);

        leftYAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.1f", value);
            }
        });

        mLineChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setTextSize(18);
        xAxis.setGridColor( Color.parseColor("#FF000000"));
        xAxis.setGridLineWidth( 1.0f );
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setValueFormatter(new ValueFormatter(){
            @Override
            public String getFormattedValue(float value) {
                return getAddedDate((long)value);
            }
        });


        mLineChart.getLegend().setEnabled(false);
        mLineChart.setDrawBorders( true );
        mLineChart.setBorderColor( Color.parseColor("#FF000000") );
        mLineChart.setBorderWidth( 1.0f );
        mLineChart.setExtraOffsets(0, 0, 40, 20);
    }

    private JsonPojo[] getData() {
        Integer inspectId = Data.getId(mItemName);

        //const val RESULT_GET_ADDRESS = BASE_URL + "index.php/result/get"
        String url = Global.RESULT_GET_ADDRESS;

        RequestBody formBody = new FormBody.Builder()
                .add("userId", mUserId.toString() )
                .add("inspectId", inspectId.toString() )
                .add("inspectDate", "" )
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post( formBody )
                .addHeader("User-Agent", "OkHttp Bot")
                .build();

        OkHttpClient okHttpClient =
                new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                Log.e(TAG, "Request is failed: " + response.code());
                return null;
            }
            if( response.body() != null ){
                String body = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                JsonPojo[] pojo = mapper.readValue(body, JsonPojo[].class);
                Log.i(TAG, "Request is succeed: " + body);
                return pojo;
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Request is failed: ", e);
            return null;
        }
    }

    private void showData(JsonPojo[] pojo) {
        List<Entry> datas = new ArrayList<>();
        Log.i(TAG, "pojo's length: " + pojo.length);
        float min = -1;
        float max = -1;

        for(int i = 0; i < pojo.length; i++) {
            try {
                JsonPojo p = pojo[i];
                Log.i(TAG, "item: " + p.getInspectDate() + ", " + p.getInspectValue());
                float x = getDateDiff(p.getInspectDate());
                float y = Float.valueOf(p.getInspectValue());
                Entry e = new Entry(x, y);
                boolean isDublicate = false;
                for (Entry ae : datas) {
                    if (ae.equalTo(e)) {
                        Log.i(TAG, "->skip(duplicate): " + x + ", " + y);
                        isDublicate = true;
                    }
                }
                if (isDublicate) {
                    continue;
                }

                datas.add(e);

                Log.i(TAG, "->entry: " + x + ", " + y);

                if (min == -1) {
                    min = x;
                } else if (min > x) {
                    min = x;
                }
                if (max == -1) {
                    max = x;
                } else if (max < x) {
                    max = x;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "->entry: failed", e);
            }
        }

        Collections.sort(datas, new EntryXComparator());

        Log.i(TAG, "---entries----------------");
        for (Entry ae: datas) {
            Log.i(TAG, ae.getX() + ", " + ae.getY());
        }
        Log.i(TAG, "--------------------------");

        this.modifyChart(datas, min, max);
    }

    private void showDummyData(int itemCount) {
        List<Entry> datas = new ArrayList<>(itemCount);
        float min = -1;
        float max = -1;
        for(int i = 0; i < itemCount; i++) {
            float base = 0;
            if (Data.getMinValue(mItemName) != null) {
                base = Data.getMinValue(mItemName);
            }
            float val = (float)(base + i * 0.01);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, i);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            float x = getDateDiff(formatter.format(calendar.getTime()));
            datas.add(new Entry(x, val));
            if (min == -1) {
                min = x;
            } else if (min > x) {
                min = x;
            }
            if (max == -1) {
                max = x;
            } else if (max < x) {
                max = x;
            }
        }

        this.modifyChart(datas, min, max);
    }

    private void modifyChart(List<Entry> datas, float min, float max) {
        XAxis xAxis = mLineChart.getXAxis();

        if (datas.size() == 1) {
            xAxis.setLabelCount(3, true);
        } else if (datas.size() == 0) {
            return;
        } else if (datas.size() <= 5 ) {
            xAxis.setLabelCount(datas.size(), true);
        } else if (datas.size() % 2 == 0) {
            int num = datas.size();
            while (num > 5 ) {
                num = num / 2;
            }
            Log.i(TAG, "setLabelCount: " + num);
            xAxis.setLabelCount( 5, true);
        } else {
            int num = datas.size();
            while (num > 5) {
                num = num / 2 + 1;
            }
            Log.i(TAG, "setLabelCount: " + num);
            xAxis.setLabelCount( 5, true);
        }

        LineDataSet lineDataSet = new LineDataSet(datas, null);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        lineData.setDrawValues(false);
        mLineChart.setData(lineData);

        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }

    private class ShowChartTask extends AsyncTask<Void, Void, JsonPojo[]> {
        @Override
        protected JsonPojo[] doInBackground(Void... voids) {
            JsonPojo[] pojos = getData();
            return pojos;
        }

        @Override
        protected void onPostExecute(JsonPojo[] result) {
            mProgressDialog.dismiss();
            if (result == null) {
                return;
            }

            //for test
            //showDummyData(55);

            showData(result);
        }
    }

    public static Long getDateDiff(String date2) {
        String strDate1 = "1970-1-1 00:00:00";
        String strDate2 = date2 + " 00:00:00";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date dtDate1;
        Date dtDate2;
        try {
            dtDate1 = formatter.parse(strDate1);
            dtDate2 = formatter.parse(strDate2);
        } catch (ParseException e) {
            return null;
        }

        long time1 = dtDate1.getTime();
        long time2 = dtDate2.getTime();

        return  (time2 - time1) / 86400000;
    }

    public static String getAddedDate(long diff) {
        String strDate1 = "1970-1-1 00:00:00";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date dtDate1;
        try {
            dtDate1 = formatter.parse(strDate1);
        } catch (ParseException e) {
            return "-";
        }

        long time2 = diff * 86400000 + dtDate1.getTime();
        Date dtDate2 = new Date(time2);

        SimpleDateFormat f = new SimpleDateFormat("MM/dd");

        return f.format(dtDate2);
    }
}
