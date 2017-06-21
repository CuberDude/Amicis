package com.darklightning.amicis.Student;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.darklightning.amicis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class StudentLoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText phoneNumText,passwordText;
    FirebaseAuth mAuth;
    Button loginButton,forgotPasswordButton;
    String emailIdOfUser,passwordOfUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        mAuth = FirebaseAuth.getInstance();
        mContext = this;
        phoneNumText = (EditText) findViewById(R.id.phone_number);
        passwordText = (EditText) findViewById(R.id.user_password);
        loginButton  = (Button) findViewById(R.id.login_button);
        forgotPasswordButton = (Button) findViewById(R.id.forgot_password);

        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    phoneNumText.setError("Invalid phone number.");
                }
                else if(e instanceof FirebaseTooManyRequestsException)
                {
                    Toast.makeText(mContext,"Quota exceeded",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            }
        };


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = task.getResult().getUser();

                    getEmailIdOfUser();
                    signInWithEmailId();


                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(mContext,"Wrong Input",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void getEmailIdOfUser()
    {
        FirebaseUser user =  mAuth.getInstance().getCurrentUser();
        emailIdOfUser = user.getEmail();
        signOut();
    }

    private void signOut()
    {
        mAuth.signOut();
        Toast.makeText(mContext,"User has Signed Out",Toast.LENGTH_LONG).show();
    }
    public void startPhoneNumberVerification(String phoneNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,60, java.util.concurrent.TimeUnit.SECONDS,this,mCallBacks);
    }
    private void signInWithEmailId()
    {
        mAuth.signInWithEmailAndPassword(emailIdOfUser, passwordOfUser)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void sendPasswordChangeMail()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = emailIdOfUser;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "email sent to ur id", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
                case R.id.login_button :
                    startPhoneNumberVerification(phoneNumText.getText().toString());
                    break;

                case R.id.forgot_password :
                    sendPasswordChangeMail();
                    break;
        }
    }
}
