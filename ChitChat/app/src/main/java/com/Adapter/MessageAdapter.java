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

import com.Model.Chat;
import com.Model.Decryption;
import com.Model.User;
import com.bumptech.glide.Glide;
import com.chitchat.hp.chitchat.MessageActivity;
import com.chitchat.hp.chitchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private Context mContext;
    private List<Chat> mChat;
    private   String imageURL;


    public MessageAdapter(Context mContext, List<Chat> mChat,String imageURL){

        this.mChat=mChat;
        this.mContext=mContext;
        this.imageURL=imageURL;



    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==MSG_TYPE_RIGHT){
            View view=LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }else
        {
            View view=LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, final int i) {
               Chat chat=mChat.get(i);

               Decryption decryption=new Decryption();



               viewHolder.show_message.setText(decryption.decrypt(chat.getMessage()));
               if (imageURL.equals("dafault")){
                   viewHolder.profile_image.setImageResource(R.drawable.ic_action_person);
               }
               else{
                   Glide.with(mContext).load(imageURL).into(viewHolder.profile_image);
               }


    }


    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;


        public ViewHolder(View itemView){
            super(itemView);

            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
        }

    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }
}

