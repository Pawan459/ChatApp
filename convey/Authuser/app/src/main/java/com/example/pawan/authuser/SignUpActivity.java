package com.example.pawan.authuser;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SignUpActivity extends AppCompatActivity {

    Button btnsignup;
    FloatingActionButton btncancel;
    EditText edtuser,edtpassword,edtfullname,edtphonenumber,edtemail;

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    View rootLayout,yourView;

    private int revealX;
    private int revealY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Intent intent = getIntent();

        rootLayout = findViewById(R.id.root_layout);

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }

        registerSession();

        btnsignup = (Button) findViewById(R.id.signup_btnsignup);
        btncancel = (FloatingActionButton) findViewById(R.id.signup_btncancel);

        edtuser = (EditText) findViewById(R.id.signup_editlogin);
        edtpassword = (EditText) findViewById(R.id.signup_editpassword);
        edtfullname=(EditText)findViewById(R.id.signup_editfullname);
        edtphonenumber=(EditText)findViewById(R.id.signup_editphonenumber);
        edtemail=(EditText)findViewById(R.id.signup_editemail);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);



        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_out,R.anim.no_animation);
            }
        });


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtuser.getText().toString();
                String password = edtpassword.getText().toString();
                if(user.length()<5 && user.startsWith("0") && user.startsWith("1") && user.startsWith("2") && user.startsWith("3") && user.startsWith("4")
                        && user.startsWith("5") && user.startsWith("6") && user.startsWith("7") && user.startsWith("8") && user.startsWith("9"))
                {
                    Toast.makeText(SignUpActivity.this, "Please Enter Valid User Name", Toast.LENGTH_SHORT).show();
                    edtuser.setText("");
                    finish();
                }


                if(edtfullname.getText().toString().length()<5 && user.startsWith("0") &&user.startsWith("1") && user.startsWith("2") && user.startsWith("3") && user.startsWith("4")
                        && user.startsWith("5") && user.startsWith("6") && user.startsWith("7") && user.startsWith("8") && user.startsWith("9"))
                {
                    Toast.makeText(SignUpActivity.this, "Please Enter Valid Full User Name", Toast.LENGTH_SHORT).show();
                    edtfullname.setText("");
                    finish();
                }
                if(edtphonenumber.getText().toString().length()!=10)
                {
                    Toast.makeText(SignUpActivity.this, "Please Enter A Valid Phone Number", Toast.LENGTH_SHORT).show();
                    edtphonenumber.setText("");
                    finish();
                }
                if(edtemail.getText().toString().trim().contains(".com")||edtemail.getText().toString().endsWith(".com"))
                {
                    edtemail.getText().toString().trim();
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Please Enter A Valid Email", Toast.LENGTH_SHORT).show();
                    finish();
                }
                QBUser qbUser = new QBUser(user, password);
                //TAKING USER FULL NAME AND PHONE NUMBER
                qbUser.setFullName(edtfullname.getText().toString().trim());
                qbUser.setPhone(edtphonenumber.getText().toString().trim());
                qbUser.setEmail(edtemail.getText().toString().trim());



                QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        Toast.makeText(getBaseContext(), "Sign Up Succesfully", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(getBaseContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

    }



    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }

    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_CIRCULAR_REVEAL_X, revealY);
        intent.putExtra(MainActivity.EXTRA_CIRCULAR_REVEAL_Y, revealX);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }








    private void registerSession() {
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Error",e.getMessage());

            }
        });
    }
}
