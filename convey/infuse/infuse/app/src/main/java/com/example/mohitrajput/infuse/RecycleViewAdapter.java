package com.example.mohitrajput.infuse;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    Context mContext;
    List<contact> mData;
    Dialog myDialog;

    public RecycleViewAdapter(Context mContext, List<contact> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_contact,parent,false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        //dialog ini

        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.dialog_contact);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));



        vHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView dailog_name_tv = (TextView) myDialog.findViewById(R.id.dialog_name);
                TextView dailog_phone_tv = (TextView) myDialog.findViewById(R.id.dialog_phone);
                ImageView dialog_contact_img = (ImageView) myDialog.findViewById(R.id.dialog_img);
                dailog_name_tv.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dailog_phone_tv.setText(mData.get(vHolder.getAdapterPosition()).getPhone());
                dialog_contact_img.setImageResource(mData.get(vHolder.getAdapterPosition()).getPhoto());

                Toast.makeText(mContext, "Text Click"+String.valueOf(vHolder.getAdapterPosition()),Toast.LENGTH_LONG).show();
                myDialog.show();;
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_phone.setText(mData.get(position).getPhone());
        holder.img.setImageResource(mData.get(position).getPhoto());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout item_contact;
        private TextView tv_name;
        private  TextView tv_phone;
        private ImageView img;

          public MyViewHolder (View itemView) {
              super(itemView);

              item_contact = (LinearLayout) itemView.findViewById(R.id.contact_item);

              tv_name = (TextView) itemView.findViewById(R.id.name_contact);
              tv_phone = (TextView) itemView.findViewById(R.id.phone_contact);
              img = (ImageView) itemView.findViewById(R.id.img_contact);
          }

    }
}
