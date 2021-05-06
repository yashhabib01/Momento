package com.example.momento;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MyProfileGirdAdapter  extends BaseAdapter {

    private List<MyProfilePostObject> myProfilePostObjects;


    public MyProfileGirdAdapter(List<MyProfilePostObject> myProfilePostObjects) {
        this.myProfilePostObjects = myProfilePostObjects;
    }

    @Override
    public int getCount() {
        return myProfilePostObjects.size();
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
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View view;


        view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item_layout,null);
            ImageView imageView = view.findViewById(R.id.momments_grid_image);


        Glide.with(parent.getContext()).load(myProfilePostObjects.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.sd)).into(imageView);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   =  new Intent(parent.getContext(), FeedDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId",myProfilePostObjects.get(position).getUi());
                bundle.putString("postID",myProfilePostObjects.get(position).getPostRef());
                intent.putExtras(bundle);
                parent.getContext().startActivity(intent);
            }
        });
     //       Glide.with(parent.getContext()).load(list.get(position)).apply(new RequestOptions().placeholder(R.drawable.sd)).into(imageView);


        return view;
    }
}
