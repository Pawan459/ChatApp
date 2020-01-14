package com.chitchat.hp.chitchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapter.MessageAdapter;
import com.Model.Chat;
import com.Model.Encryption;
import com.Model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    ImageButton btn_send;
    EditText txt_send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btn_send=(ImageButton)findViewById(R.id.btn_send);
        txt_send=(EditText)findViewById(R.id.txt_send);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        intent=getIntent();
        final String userId=intent.getStringExtra("userId");

        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar );
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,MainActivity.class));
            }
        });

        profile_image=(CircleImageView)findViewById(R.id.profile_image);
        username=(TextView)findViewById(R.id.username);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Users").child(userId);



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               User user=dataSnapshot.getValue(User.class);
               username.setText(user.getUsername());
               if(user.getImageURL().equals("dafault")){
                   profile_image.setImageResource(R.drawable.ic_action_person);
               }
               else {
                   Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
               }

               readMessage(firebaseUser.getUid(),userId,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String message=txt_send.getText().toString();
                Encryption encryption=new Encryption();

                String message=encryption.encypt(txt_send.getText().toString().trim(),username.getText().toString().charAt(0));

                if (!message.equals("")){
                    sendMessage(firebaseUser.getUid(),userId,message);
                }else
                {
                    Toast.makeText(getApplicationContext(), "Please write Something", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendMessage(String sender,String reciever,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);
        txt_send.setText("");

    }
    private  void readMessage(final String myId, final String userId, final String imageURL){
        mChat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);


                    if (chat.getReciever().equals(myId)&&chat.getSender().equals(userId)||chat.getReciever().equals(userId)&&chat.getSender().equals(myId)){
                        mChat.add(chat);
                    }

                    messageAdapter=new MessageAdapter(MessageActivity.this,mChat,imageURL);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//    private  void status(String status){
//        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
//
//        HashMap<String,Object> hashMap=new HashMap<>();
//        hashMap.put("status",status);
//        reference.updateChildren(hashMap);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        status("online");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        status("offline");
//    }
}
