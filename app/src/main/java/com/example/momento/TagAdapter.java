package com.example.momento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private List<TagObject> tagObjectsList;
    public static  List<String> checkTagUsers = new ArrayList<>();
    Context context;
    int lastposition = -1;

    public TagAdapter(List<TagObject> tagObjectsList) {
        this.tagObjectsList = tagObjectsList;
    }

    @NonNull
    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagAdapter.ViewHolder holder, int position) {
        holder.setData(tagObjectsList.get(position).getImage(),tagObjectsList.get(position).getName());

        if(checkTagUsers.contains(tagObjectsList.get(holder.getLayoutPosition()).getUid())){
            holder.checkBox.setChecked(true);
        }
        if(lastposition < holder.getAdapterPosition()) {
            Animation animation = AnimationUtils.loadAnimation(((ViewHolder) holder).itemView.getContext(), R.anim.tag);
            ((ViewHolder) holder).itemView.startAnimation(animation);
            lastposition = holder.getAdapterPosition();
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean  receive = !tagObjectsList.get(holder.getLayoutPosition()).getCheck();
                tagObjectsList.get(holder.getLayoutPosition()).setCheck(receive);
                if(receive){
                        checkTagUsers.add(tagObjectsList.get(holder.getLayoutPosition()).getUid());
                }else{
                    checkTagUsers.remove(tagObjectsList.get(holder.getLayoutPosition()).getUid());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagObjectsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImage;
        private TextView userName;
        private CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.tag_user_image);
            userName = itemView.findViewById(R.id.tag_user_name);
            checkBox = itemView.findViewById(R.id.tag_user_check);



        }

        private void setData(String image, String name){
            Glide.with(itemView.getContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.ic_account_circle_24)).into(userImage);
            userName.setText(name);
        }
    }
}
