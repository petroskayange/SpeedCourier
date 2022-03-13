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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button signUpButton;
    private int mStatusCode = 0;
    private EditText username, loginPassword;
    public String ip;
    SharedPreferences sharedPreferences, sharedPreferences2;
    private DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
//        sharedPreferences2 = getSharedPreferences("myprefIPAddress",MODE_PRIVATE);
        signUpButton = findViewById(R.id.signupbtn);
        username = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginpassword);
        ip = sharedPreferences.getString("ipAddress",null);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SingUpActivity.class));
            }
        });

    }

    public void loginUser(View view){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://"+ip+"/php_api/api/user/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("onResponse", response);
                        Log.e("statusCode", String.valueOf(mStatusCode));
                        if(mStatusCode == 200)
                        {
                            Toast.makeText(getBaseContext(), "Successfully Loged In", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userKey",response);
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            editor.apply();
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Wrong Email and Password", Toast.LENGTH_SHORT).show();
                Log.e("Login", String.valueOf(error));
            }
        }) {

            protected Map<String, String> getParams() {
                Map<String, String> paramVal = new HashMap<>();
                paramVal.put("username", username.getText().toString());
                paramVal.put("password", loginPassword.getText().toString());
                return paramVal;
            }
            @Override
            protected Response<String> parseNetworkResponse (NetworkResponse response){
                if (response != null) {
                    mStatusCode = response.statusCode;
                }
                return super.parseNetworkResponse(response);
            }

        };

        queue.add(stringRequest);

    }

}