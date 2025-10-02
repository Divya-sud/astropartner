package com.axis.helloastropartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    Button registerBtn,registerBtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerBtn = findViewById(R.id.buttonregister);
       // recognizeBtn = findViewById(R.id.buttonrecognize);
        registerBtn1 = findViewById(R.id.buttonregister1);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        }); 

        registerBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,AccessActivity.class));
            }
        });

       // recognizeBtn.setOnClickListener(new View.OnClickListener() {
       //     @Override
        //    public void onClick(View view) {
        //        startActivity(new Intent(LoginActivity.this,RecognitionActivity.class));
        //    }
      //  });
    }
}