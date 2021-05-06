package com.example.momento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CreateGroupAdapter  extends RecyclerView.Adapter<CreateGroupAdapter.ViewHolders> {
    private List<TagObject> tagObjectList;

    public CreateGroupAdapter(List<TagObject> tagObjectList) {
        this.tagObjectList = tagObjectList;
    }

    @NonNull
    @Override
    public CreateGroupAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item_layout,parent,false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CreateGroupAdapter.ViewHolders holder, int position) {
        holder.setData(tagObjectList.get(position).getImage(),tagObjectList.get(position).getName());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean  receive = !tagObjectList.get(holder.getLayoutPosition()).getCheck();
                tagObjectList.get(holder.getLayoutPosition()).setCheck(receive);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tagObjectList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        private ImageView userImage;
        private TextView userName;
        private CheckBox checkBox;
        public ViewHolders(@NonNull View itemView) {
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
