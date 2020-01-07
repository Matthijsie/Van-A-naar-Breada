package com.example.vananaarbreda.Activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vananaarbreda.R;
import com.example.vananaarbreda.Route.Coordinate;

import java.util.List;
import java.util.zip.CheckedOutputStream;

public class SightListAdapter extends RecyclerView.Adapter<SightListAdapter.SightListViewHolder> {

    private static final String TAG = SightListAdapter.class.getSimpleName();
    private List<Coordinate> dataset;
    private Context context;

    public SightListAdapter(List<Coordinate> dataset, Context context){
        this.dataset = dataset;
        this.context = context;
        Log.d(TAG, "SightListAdapter Created");
    }

    @NonNull
    @Override
    public SightListAdapter.SightListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.row_item, parent, false);
        return new SightListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SightListAdapter.SightListViewHolder holder, int position) {
        Coordinate coordinate = dataset.get(position);

        holder.textViewSight.setText(coordinate.getSight().getName());
        if (coordinate.getSight().getBitmapImages().size() > 0) {
            ImageView image = holder.imageViewSight;
            image.setId(0);
            image.setPadding(50, 2, 50, 2);

            int resID = context.getResources().getIdentifier(dataset.get(position).getSight().getStringImageNames().get(0), "drawable", context.getPackageName());
            image.setImageResource(resID);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setAdjustViewBounds(true);
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class SightListViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewSight;
        private TextView textViewSight;

        public SightListViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewSight = itemView.findViewById(R.id.imageViewSightItem);
            textViewSight = itemView.findViewById(R.id.textViewSightItem);
        }
    }
}
