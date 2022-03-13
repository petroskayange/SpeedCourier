package com.example.speedcourier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserPayments extends HomeActivity {
    EditText productID;
    private int mStatusCode = 0;
    TextView productName, QTY, Destination, amount;
    String status;
    String checkAmount = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payments);

        productID = findViewById(R.id.product_id);
        productName = findViewById(R.id.productName);
        QTY = findViewById(R.id.productQty);
        Destination = findViewById(R.id.productDestination);
        amount = findViewById(R.id.totalProductAmount);
    }

    public void submitUPayment(View view) {
        startActivity(new Intent(UserPayments.this, HomeActivity.class));
    }
    public void findProduct(View view){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://"+ip+"/php_api/api/post/read_single.php?referenceNumber="+productID.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("onResponse", response);
                        Log.e("statusCode", String.valueOf(mStatusCode));
                        if(mStatusCode == 200)
                        {
                            Toast.makeText(getBaseContext(), "Product Found", Toast.LENGTH_SHORT).show();
                            Log.e("in side product", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                productName.setText("Product name: "+ jsonObject.getString("name"));
                                QTY.setText("Quantity: "+ jsonObject.getString("quantity"));
                                Destination.setText("Product Destination: "+ jsonObject.getString("destination"));
                                amount.setText("Total Amount: "+ jsonObject.getString("amount"));

                                checkAmount = jsonObject.getString("amount");
                                status =jsonObject.getString("status");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Product not found", Toast.LENGTH_SHORT).show();
                Log.e("Login", String.valueOf(error));
            }
        }) {

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

    public void submitOnlinePayment(View view) {
        if(checkAmount != "") {
            if (!status.equals("Cleared")) {
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "http://" + ip + "/php_api/api/post/update.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("onResponse", response);
                                Log.e("statusCode", String.valueOf(mStatusCode));
                                if (mStatusCode == 200) {
                                    Toast.makeText(getBaseContext(), "Successfully made payment", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UserPayments.this, HomeActivity.class));
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Failed to make payment", Toast.LENGTH_SHORT).show();
                        Log.e("Login", String.valueOf(error));
                    }
                }) {

                    protected Map<String, String> getParams() {
                        Map<String, String> paramVal = new HashMap<>();
                        paramVal.put("referenceNumber", productID.getText().toString());
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
            } else {
                Toast.makeText(getBaseContext(), "The product is already payed for", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getBaseContext(), "Please find the product first", Toast.LENGTH_SHORT).show();
        }
    }

}