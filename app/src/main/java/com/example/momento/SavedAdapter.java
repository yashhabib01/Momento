package com.example.momento;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SavedAdapter extends BaseAdapter {

    private List<MyProfilePostObject> myProfilePostObjectList;

    public SavedAdapter(List<MyProfilePostObject> myProfilePostObjectList) {
        this.myProfilePostObjectList = myProfilePostObjectList;
    }

    @Override
    public int getCount() {
        return myProfilePostObjectList.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final View view;
        view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item_layout,null);

        ImageView imageView = view.findViewById(R.id.momments_grid_image);

        Glide.with(parent.getContext()).load(myProfilePostObjectList.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.sd)).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   =  new Intent(parent.getContext(), FeedDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId",myProfilePostObjectList.get(position).getUi());
                bundle.putString("postID",myProfilePostObjectList.get(position).getPostRef());
                intent.putExtras(bundle);
                parent.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
