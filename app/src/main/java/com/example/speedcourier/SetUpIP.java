package com.example.speedcourier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetUpIP extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText ipAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_ip);
        sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        ipAddress = findViewById(R.id.newIPAddress);


    }

    public void SubmitIPAddress(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ipAddress", ipAddress.getText().toString());
        editor.apply();
        Toast.makeText(getBaseContext(), "Ip address saved successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SetUpIP.this, MainActivity.class));
    }
}