package com.axis.helloastropartner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;

public class MainActivity extends AppCompatActivity {

    private Button mSendOtpBtn;
    private EditText mNumberText;
    private CountryCodePicker countryCodePicker;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String finalPhoneNumber;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    private TextView cuisineTextLabel3;
    private String[] messages = {
            "Partner with us to bring celestial insight to your clientele",
            "Expand your destiny with our trusted astrology brand.",
            "A business alliance written in the stars."
    };

    private int index = 0;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changestatusbarcolor();
        initialiseViews();

        mSendOtpBtn.setOnClickListener(view -> {
            String phoneNumber = mNumberText.getText().toString();
            String ccp = countryCodePicker.getSelectedCountryCodeWithPlus();

            finalPhoneNumber = ccp + phoneNumber;

            if (phoneNumber.isEmpty()
                    || ccp.isEmpty()
            )
            {
                Toast.makeText(this, "Please Enter Correct Credentials", Toast.LENGTH_LONG).show();
            }
            else {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        finalPhoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        this,
                        mCallBacks
                );

            }

        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(MainActivity.this, "Verification Failed, please try again!", Toast.LENGTH_LONG).show();
                //  signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Intent otpIntent = new Intent(MainActivity.this, AuthOtpActivity.class);
                otpIntent.putExtra("AuthCredentials", s);
                otpIntent.putExtra("PhoneNumber", finalPhoneNumber);
                startActivity(otpIntent);
            }
        };

        cuisineTextLabel3 = findViewById(R.id.cuisineTextLabel3);

        runnable = new Runnable() {
            @Override
            public void run() {
                cuisineTextLabel3.setText(messages[index]);
                index = (index + 1) % messages.length;
                handler.postDelayed(this, 3000); // Change text every 3 seconds
            }
        };

        handler.post(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Prevent memory leaks
    }
    private void initialiseViews() {
        mSendOtpBtn = findViewById(R.id.sendOtpBtn);
        mNumberText = findViewById(R.id.loginInput);
        countryCodePicker = findViewById(R.id.countryCodeHolder);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
    }
    private void changestatusbarcolor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                       // sendUserToMain();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}