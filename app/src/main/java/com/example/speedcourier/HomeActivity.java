package com.example.speedcourier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    public String userData,username,loginID,Contact,Address,Email,LastName,firstName,role,ip;
    TextView userRole, fullname;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userRole = findViewById(R.id.userRole);
        fullname = findViewById(R.id.userFullname);
        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        ip = sharedPreferences.getString("ipAddress",null);

        userData = sharedPreferences.getString("userKey",null);
        if(userData != null)
        getSharedPref();
    }
    public  void getSharedPref(){
        try {
            JSONObject jsonObject = new JSONObject(userData);
            firstName =jsonObject.getString("firstName");
            LastName =jsonObject.getString("LastName");
            Email =jsonObject.getString("Email");
            Address =jsonObject.getString("Address");
            Contact =jsonObject.getString("Contact");
            loginID =jsonObject.getString("loginID");
            username =jsonObject.getString("username");
            role =jsonObject.getString("role");

            userRole.setText(role+" ");
            fullname.setText("Welcome "+firstName+" "+LastName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("share pre", firstName);
    }
    public void displayTracking(View view) {
        startActivity(new Intent(HomeActivity.this, TrackParcel.class));
    }

    public void displaySettings(View view) {
        startActivity(new Intent(HomeActivity.this, UserProfile.class));
    }

    public void displayHome(View view) {
        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
    }

    public void displayService(View view) {
        startActivity(new Intent(HomeActivity.this, Services.class));
    }

    public void displayCalculationa(View view) {
        Log.e("roless",role);
        if(role.equals("Admin"))
        startActivity(new Intent(HomeActivity.this, Payments.class));
        else
            startActivity(new Intent(HomeActivity.this, UserPayments.class));
    }
}