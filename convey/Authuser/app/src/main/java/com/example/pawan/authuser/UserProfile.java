package com.example.pawan.authuser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pawan.authuser.Common.Common;
import com.example.pawan.authuser.Holder.QBUsersHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestUpdateBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UserProfile extends AppCompatActivity {

    EditText edtPassword,edtOldPassword,edtFullName,edtEmail,edtPhone;
    Button btnUpdate,btnCancel;

    ImageView user_avatar;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_update_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId()))
        {
            case R.id.user_update_log_out:
                logOut();
                break;
            default:
                break;
        }
        return true;
    }

    private void logOut() {
        QBUsers.signOut().performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                //Completely logging it out that's why getting instance by calling chatservice
                QBChatService.getInstance().logout(new QBEntityCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, Bundle bundle) {
                        Toast.makeText(UserProfile.this, "You Are Log Out !!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserProfile.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//remove all previous activities
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //add toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.user_update_toolbar);
        toolbar.setTitle("In Fuse");
        setSupportActionBar(toolbar);


        initViews();

        loadUserProfile();

        user_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.SELECT_PICTURE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtPassword.getText().toString();
                String oldpassword=edtOldPassword.getText().toString();
                String email=edtEmail.getText().toString();
                String phone=edtPhone.getText().toString();
                String fullname=edtFullName.getText().toString();

                QBUser user = new QBUser();
                user.setId(QBChatService.getInstance().getUser().getId());
                if(!Common.isNullOrEmptyString(oldpassword))
                    user.setOldPassword(oldpassword);
                if(!Common.isNullOrEmptyString(password))
                    user.setPassword(password);
                if(!Common.isNullOrEmptyString(fullname))
                    user.setFullName(fullname);
                if(!Common.isNullOrEmptyString(email))
                    user.setEmail(email);
                if(!Common.isNullOrEmptyString(phone))
                    user.setPhone(phone);

                final ProgressDialog mDialog = new ProgressDialog(UserProfile.this);
                mDialog.setMessage("Please Wait....");
                mDialog.show();
                QBUsers.updateUser(user).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        Toast.makeText(UserProfile.this, "User"+qbUser.getLogin()+" Updated", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();


                    }

                    @Override
                    public void onError(QBResponseException e) {

                        Toast.makeText(UserProfile.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            if(requestCode ==Common.SELECT_PICTURE)
            {
                Uri selectedImageUri = data.getData();
                final ProgressDialog mDialog = new ProgressDialog(UserProfile.this);
                mDialog.setMessage("Please Wait...");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();


                try{
                    //convert uri to file
                    InputStream in = getContentResolver().openInputStream(selectedImageUri);
                    final Bitmap bitmap = BitmapFactory.decodeStream(in);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
                    File file = new File(Environment.getExternalStorageDirectory()+"/myimage.png");
                    FileOutputStream fileOut =  new FileOutputStream(file);
                    fileOut.write(bos.toByteArray());
                    fileOut.flush();
                    fileOut.close();

                    final int imageSizeKb = (int)file.length()/1024;
                    if(imageSizeKb>=(1024*100))
                    {
                        Toast.makeText(this, "Image Size Not Acceptable", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Upload File
                    QBContent.uploadFileTask(file,true,null)
                            .performAsync(new QBEntityCallback<QBFile>() {
                                @Override
                                public void onSuccess(QBFile qbFile, Bundle bundle) {
                                    //set user avatar
                                    QBUser user = new QBUser();
                                    user.setId(QBChatService.getInstance().getUser().getId());
                                    user.setFileId(Integer.parseInt(qbFile.getId().toString()));



                                    //Upload user
                                    QBUsers.updateUser(user)
                                            .performAsync(new QBEntityCallback<QBUser>() {
                                                @Override
                                                public void onSuccess(QBUser qbUser, Bundle bundle) {
                                                    mDialog.dismiss();
                                                    user_avatar.setImageBitmap(bitmap);
                                                }

                                                @Override
                                                public void onError(QBResponseException e) {

                                                }
                                            });

                                }

                                @Override
                                public void onError(QBResponseException e) {

                                }
                            });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void loadUserProfile() {
        //Load Avatar
        QBUsers.getUser(QBChatService.getInstance().getUser().getId())
                .performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        //save to cache
                        QBUsersHolder.getInstance().putUser(qbUser);
                        if(qbUser.getFileId() != null)
                        {
                            int profilePicture = qbUser.getFileId();

                            QBContent.getFile(profilePicture)
                                    .performAsync(new QBEntityCallback<QBFile>() {
                                        @Override
                                        public void onSuccess(QBFile qbFile, Bundle bundle) {
                                            String fileURL=qbFile.getPublicUrl();
                                            Picasso.with(getBaseContext())
                                                    .load(fileURL)
                                                    .into(user_avatar);
                                        }

                                        @Override
                                        public void onError(QBResponseException e) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });


        QBUser currentuser = QBChatService.getInstance().getUser();
        String fullname = currentuser.getFullName();
        String email = currentuser.getEmail();
        String Phone = currentuser.getPhone();

        edtEmail.setText(email);
        edtFullName.setText(fullname);
        edtPhone.setText(Phone);
    }

    private void initViews() {
    btnCancel=(Button)findViewById(R.id.update_btn_cancel);
    btnUpdate=(Button)findViewById(R.id.update_btn_update);

    edtEmail=(EditText)findViewById(R.id.update_edt_email);
    edtFullName=(EditText)findViewById(R.id.update_edt_full_name);
    edtPhone=(EditText)findViewById(R.id.update_edt_phone);
    edtPassword=(EditText)findViewById(R.id.update_edt_password);
    edtOldPassword=(EditText)findViewById(R.id.update_edt_old_password);

    user_avatar=(ImageView)findViewById(R.id.user_avatar);

    }
}
