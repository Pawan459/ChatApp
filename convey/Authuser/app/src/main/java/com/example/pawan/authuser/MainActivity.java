package com.example.pawan.authuser;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pawan.authuser.Holder.QBUsersHolder;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.request.QBDialogRequestBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.util.MAC;

public class MainActivity extends AppCompatActivity {

    static final String APP_ID = "72797";
    static final String AUTH_KEY = "6adpnHhhEz3adpr";
    static final String AUTH_SECRET = "PrQtJLBhXxk4vQP";
    static final String ACCOUNT_KEY = "YMVmYM47Zq1Ys6xsmZnk";

    static final  int REQUEST_CODE=1000;

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";



    private int revealX;
    private int revealY;


    Button btnlogin,btnforgot;
    FloatingActionButton btnsignup;
    TextInputEditText edtuser,edtpassword;
    EditText newemail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //requestRuntimePermission();


        initializeFramework();


            btnlogin = (Button) findViewById(R.id.main_btnlogin);
            btnsignup = (FloatingActionButton) findViewById(R.id.main_btnsignup);

            edtpassword = (TextInputEditText) findViewById(R.id.main_editpassword);
            edtuser = (TextInputEditText) findViewById(R.id.main_editlogin);



            btnsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presentActivity(view);
                }
            });


            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String user = edtuser.getText().toString();
                    final String password = edtpassword.getText().toString();


                    QBUser qbUser = new QBUser(user, password);


                    //LOGIN Button
                    QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser, Bundle bundle) {
                            Toast.makeText(getBaseContext(), "Login Succesfully", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(MainActivity.this, ChatDialogsActivity.class);
                            intent.putExtra("user", user);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            finish(); //Close login activity after logged
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Toast.makeText(getBaseContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            });
        }





    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(SignUpActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(SignUpActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }



   /* @TargetApi(Build.VERSION_CODES.M)
    private void requestRuntimePermission() {
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
            || (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED))
        {
            requestPermissions(new String[]{
                  Manifest.permission.READ_EXTERNAL_STORAGE,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE
            },REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_CODE:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getBaseContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }*/

    //TAKING AUTHORIZATION
    private void initializeFramework() {
        QBSettings.getInstance().init(getApplicationContext(),APP_ID,AUTH_KEY,AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }
}
