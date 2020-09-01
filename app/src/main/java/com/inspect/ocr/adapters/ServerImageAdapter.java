package com.inspect.ocr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inspect.ocr.models.PatientImage;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.inspect.ocr.R;

import okhttp3.OkHttpClient;

import static com.inspect.ocr.common.Common.cm;

public class ServerImageAdapter extends RecyclerView.Adapter<ServerImageAdapter.ViewHolder> {

    private List<PatientImage> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, PatientImage obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ServerImageAdapter(Context context, List<PatientImage> items) {
        this.items = items;
        ctx = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final PatientImage c = items.get(position);

        if (c.getChecked())
            holder.checkedImg.setVisibility(View.VISIBLE);
        else
            holder.checkedImg.setVisibility(View.GONE);

        holder.loadingView.setVisibility(View.VISIBLE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .build();

        Picasso picasso = new Picasso.Builder(ctx )
                .memoryCache(new LruCache(100000000))
                .downloader( new OkHttp3Downloader( okHttpClient ) )
                .build();

        picasso.get()
                .load(c.getThumbImagePath().replace("localhost", "192.168.1.111"))
                .placeholder(R.drawable.image_placeholder)
                .resize(200,266 )
                .centerInside()
                .into( holder.receiptImg, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        //do smth when picture is loaded successfully
                        holder.loadingView.setVisibility(View.GONE);
                        //holder.patientIdTxt.setText( "成功" );
                    }
                    @Override
                    public void onError(Exception ex) {
                        holder.patientIdTxt.setText( ex.getMessage() );
                        //do smth when there is picture loading error
                        //holder.loadingView.setVisibility(View.GONE);
                    }
                });

        holder.patientIdTxt.setText(c.getPatientId());
        holder.receiptDateTxt.setText(cm.convertStringFromTime(c.getReceiptDate()));

        holder.receiptImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, c, position);
                }
            }
        });
    }

    public void setListData(List<PatientImage> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void resetListData() {
        this.items = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView receiptImg;
        public ImageView checkedImg;
        public TextView patientIdTxt, receiptDateTxt;
        public ProgressBar loadingView;

        public ViewHolder(View v) {
            super(v);
            receiptImg = v.findViewById(R.id.image_view);
            checkedImg = v.findViewById(R.id.check_img);
            patientIdTxt = v.findViewById(R.id.patient_id_value);
            receiptDateTxt = v.findViewById(R.id.receipt_date_value);
            loadingView = v.findViewById(R.id.loading_spinner);
        }
    }
}
