package com.axis.helloastropartner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.Objects;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class AuthOtpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String mAuthVerificationId, phoneNum, deviceToken,uid;
    private OtpTextView mOtpText;
    private Button mVerifyBtn;
    TextView numb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_otp);

        changestatusbarcolor();

        numb = findViewById(R.id.num);
        String nu = numb.getText().toString();
        Intent intent = getIntent();
        if (intent!=null){
            mAuthVerificationId = getIntent().getStringExtra("AuthCredentials");
            phoneNum = getIntent().getStringExtra("PhoneNumber");
            numb.setText(phoneNum);
        }
        //  mAuthVerificationId = getIntent().getStringExtra("AuthCredentials");
        //  phoneNum = getIntent().getStringExtra("PhoneNumber");
        init();
        mOtpText.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {

                otp = mOtpText.getOTP();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, otp);
                signInWithPhoneAuthCredential(credential);
            }
        });

        mVerifyBtn.setOnClickListener(view -> {
            String otp = mOtpText.getOTP();
            if (otp.isEmpty()){
                Toast.makeText(this, "Please enter correct OTP", Toast.LENGTH_LONG).show();
            }else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, otp);
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        //   uid = firebaseUser.getUid();
        mOtpText = findViewById(R.id.otpView);
        TextView mUserNum = findViewById(R.id.userNum);
        mUserNum.setText("We have sent you an OTP at " + phoneNum + " please enter the OTP.");
        mVerifyBtn = findViewById(R.id.verifyOTP);
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

                        FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                        String uid = Objects.requireNonNull(user).getUid();
                        final FirebaseFirestore db = FirebaseFirestore.getInstance();
                        final DocumentReference docRef = db.collection("UserList").document(uid);

                        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                deviceToken = task1.getResult().getToken();
                            }
                        });

                        docRef.get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Intent intent = new Intent(AuthOtpActivity.this, LoginActivity.class);
                                intent.putExtra("PhoneNumber", phoneNum);
                                intent.putExtra("UID", mAuthVerificationId);
                                intent.putExtra("TOKEN", deviceToken);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                //sendUserToMain();
                            }else {
                                Intent intent = new Intent(AuthOtpActivity.this, LoginActivity.class);
                                intent.putExtra("PHONENUMBER", phoneNum);
                                intent.putExtra("UID", mAuthVerificationId);
                                intent.putExtra("TOKEN", deviceToken);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                        });

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}