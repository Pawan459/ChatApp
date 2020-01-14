package com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Model.User;
import com.bumptech.glide.Glide;
import com.chitchat.hp.chitchat.MessageActivity;
import com.chitchat.hp.chitchat.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context mContext;
    private List<User> mUsers;
    private boolean isChat;


    public UserAdapter(Context mContext, List<User> mUsers,boolean isChat){

        this.mUsers=mUsers;
        this.mContext=mContext;
        this.isChat=isChat;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.user_item,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final User user=mUsers.get(i);
        viewHolder.username.setText(user.getUsername());
        if (user.getImageURL().equals("dafault")){
            viewHolder.profile_image.setImageResource(R.drawable.ic_action_person);
        }
        else {
            Glide.with(mContext).load(user.getImageURL()).into(viewHolder.profile_image);
        }
       if (isChat){
            if (user.getStatus().equals("online")){
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
            }else
            {
                viewHolder.img_off.setVisibility(View.VISIBLE);
                viewHolder.img_on.setVisibility(View.GONE);
            }
       }else {
           viewHolder.img_off.setVisibility(View.GONE);
           viewHolder.img_on.setVisibility(View.GONE);
       }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,MessageActivity.class);
                intent.putExtra("userId",user.getId());
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private  ImageView img_off;


        public ViewHolder(View itemView){
            super(itemView);

            username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);
            img_off=itemView.findViewById(R.id.img_off);
            img_on=itemView.findViewById(R.id.img_on);
        }

    }

}
