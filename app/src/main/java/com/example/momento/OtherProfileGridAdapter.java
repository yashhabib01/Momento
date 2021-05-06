package com.example.momento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class OtherProfileGridAdapter extends BaseAdapter {
    private List<String> list;

    public OtherProfileGridAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       final  View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item_layout,null);

        ImageView imageView = view.findViewById(R.id.momments_grid_image);

        Glide.with(parent.getContext()).load(list.get(position)).apply(new RequestOptions().placeholder(R.drawable.sd)).into(imageView);
        return view;
    }
}
