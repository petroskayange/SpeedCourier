package com.example.speedcourier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SingUpActivity extends MainActivity {

    private EditText email, username, password, confirmPassword, firstname, surname,phone, address;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button signUpButton;
    private int mStatusCode = 0;
    private  DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        confirmPassword = findViewById(R.id.confirmPassword);
        password = findViewById(R.id.password);
        firstname = findViewById(R.id.firstname);
        surname = findViewById(R.id.surname);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        radioGroup = (RadioGroup) findViewById(R.id.role);


        signUpButton = findViewById(R.id.signupbtn);
        myDB = new DatabaseHelper(this);
    }

    public void createUser(View view) {
        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://"+ip+"/php_api/api/user/create_user.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("onResponse", response);
                            Log.e("statusCode", String.valueOf(mStatusCode));
                            if (mStatusCode == 200) {
                                Toast.makeText(getBaseContext(), "Successfully Created User", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SingUpActivity.this, MainActivity.class));
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Failling to send to api", String.valueOf(error));
                    Toast.makeText(getBaseContext(), "Fail to Create User", Toast.LENGTH_SHORT).show();
                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> paramVal = new HashMap<>();
                    paramVal.put("Email", email.getText().toString());
                    paramVal.put("username", username.getText().toString());
                    paramVal.put("password", password.getText().toString());
                    paramVal.put("role", radioButton.getText().toString());
                    paramVal.put("firstName", firstname.getText().toString());
                    paramVal.put("LastName", surname.getText().toString());
                    paramVal.put("Address", address.getText().toString());
                    paramVal.put("Contact", phone.getText().toString());
                    return paramVal;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response != null) {
                        mStatusCode = response.statusCode;
                    }
                    return super.parseNetworkResponse(response);
                }
            };

            queue.add(stringRequest);
        }else
            Toast.makeText(getBaseContext(), "The passwords are not matching", Toast.LENGTH_SHORT).show();
    }
}