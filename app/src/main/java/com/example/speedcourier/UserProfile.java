package com.example.speedcourier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserProfile extends HomeActivity {
    TextView profileEmail,profileFirstname,profileLastname,profilePhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileEmail= findViewById(R.id.profileEmail);
        profileFirstname= findViewById(R.id.profileFirstname);
        profileLastname= findViewById(R.id.profileLastname);
        profilePhone= findViewById(R.id.profilePhone);

        profileEmail.setText(Email);
        profileFirstname.setText(firstName);
        profileLastname.setText(LastName);
        profilePhone.setText(Contact);
    }

    public void logout(View view) {
        startActivity(new Intent(UserProfile.this, MainActivity.class));
    }
}