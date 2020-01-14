package com.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.Model.User;
import com.bumptech.glide.Glide;
import com.chitchat.hp.chitchat.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
  CircleImageView profile_image;
  TextView username;
  private static int WE=5000;

  DatabaseReference reference;
  FirebaseUser fUser;

  StorageReference storageReference;
  private static final int IMAGE_REQUEST=1;
  private Uri imageUri;
  private StorageTask uploadTask;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        fUser=FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
        storageReference=FirebaseStorage.getInstance().getReference("Uploads");

        profile_image=view.findViewById(R.id.profile_image);
        username=view.findViewById(R.id.username);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        username.setText(user.getUsername());
                        if (user.getImageURL().equals("dafault")){
                            profile_image.setImageResource(R.drawable.ic_action_person);
                        }
                        else {
                            Glide.with(getContext()).load(user.getImageURL()).into(profile_image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//            }
//        },WE);


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        profile_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "Hii there!", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        return  view;
    }

    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);



    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void uploadImage(){

        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Uploading...");
        pd.show();


        if (imageUri!=null){
            final StorageReference fileReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            uploadTask=fileReference.putFile(imageUri);
           uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
               @Override
               public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                   if (!task.isSuccessful()){
                       throw task.getException();
                   }


                   return fileReference.getDownloadUrl();
               }
           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {
                   if(task.isSuccessful()){
                       Uri downloadUri =task.getResult();
                       String mUri=downloadUri.toString();

                       reference=FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
                       HashMap<String,Object> hashMap=new HashMap<>();
                       hashMap.put("imageURL",mUri);
                       reference.updateChildren(hashMap);

                       pd.dismiss();
                   }else{
                       Toast.makeText(getContext(), "Failed..", Toast.LENGTH_SHORT).show();
                       pd.dismiss();
                   }
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                   pd.dismiss();
               }
           });
        }else
        {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            if (uploadTask!=null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in Progrss", Toast.LENGTH_SHORT).show();
            }else
            {
                uploadImage();
            }

        }
    }


}
