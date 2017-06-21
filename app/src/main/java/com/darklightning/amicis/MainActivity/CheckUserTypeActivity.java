package com.darklightning.amicis.MainActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.darklightning.amicis.College.CollegeRegisterActivity;
import com.darklightning.amicis.R;
import com.darklightning.amicis.Student.StudentRegisterActivity;

public class CheckUserTypeActivity extends AppCompatActivity implements View.OnClickListener{

    Button collegeLoginButton,studentLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user_type);
        collegeLoginButton = (Button) findViewById(R.id.college_login);
        studentLoginButton = (Button) findViewById(R.id.student_login);

        collegeLoginButton.setOnClickListener(this);
        studentLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.college_login :
                Intent intent = new Intent(this,CollegeRegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.student_login :
                Intent intent1 = new Intent(this,StudentRegisterActivity.class);
                startActivity(intent1);
                break;

        }
    }
}
