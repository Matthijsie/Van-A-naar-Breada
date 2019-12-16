package com.example.vananaarbreda.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vananaarbreda.R;
import com.example.vananaarbreda.Route.Route;

import java.util.List;

public class CustomListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Route> routes;
    private LayoutInflater inflater;

    public CustomListViewAdapter(Context context, List<Route> routes) {

        this.routes = routes;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public Object getItem(int position) {
        return routes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.routes_dropdown_item, null);
        TextView names = convertView.findViewById(R.id.textViewSpinnerItem);
        names.setText(routes.get(position).getName());
        return convertView;
    }
}
