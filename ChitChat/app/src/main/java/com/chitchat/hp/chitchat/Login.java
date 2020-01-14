package com.chitchat.hp.chitchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    MaterialEditText username,password,email;
    Button register;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    TextView txt_login;
    public String usernamee;
    public String passwordd;
    public String emaill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        username=(MaterialEditText)findViewById(R.id.username);
        password=(MaterialEditText)findViewById(R.id.password);
        email=(MaterialEditText)findViewById(R.id.email);
        register=(Button)findViewById(R.id.btn_register);
        txt_login=(TextView)findViewById(R.id.txt_login);
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,sign_in.class);
                startActivity(i);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });
    }

    public void registerUser()
    {
         usernamee=username.getText().toString().trim();
         passwordd=password.getText().toString().trim();
         emaill=email.getText().toString().trim();
        if (TextUtils.isEmpty(usernamee) ||  TextUtils.isEmpty(passwordd) || TextUtils.isEmpty(emaill) )
        {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
        }else if(passwordd.length()<6){
            Toast.makeText(getApplicationContext(), "password must be at least six characters", Toast.LENGTH_SHORT).show();


        }else
        {
            mAuth.createUserWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser =mAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String userId=firebaseUser.getUid();

                        reference=FirebaseDatabase.getInstance().getReference("Users").child(userId);

                        HashMap<String, String> hashMap=new HashMap<>();
                        hashMap.put("id",userId);
                        hashMap.put("username",usernamee);
                        hashMap.put("imageURL","dafault");
                        hashMap.put("status","offline");

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Intent i =new Intent(Login.this,MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "you can not register with this email and password", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null)
        {
            Intent intent=new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
