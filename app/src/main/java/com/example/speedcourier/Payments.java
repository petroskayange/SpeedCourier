package com.example.speedcourier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Payments extends HomeActivity {
    private EditText receiverPhone,weightText,productNameText,quantityText,descriptionText,userIDText;
    private String fromText, toText;
    private int mStatusCode = 0;
    private String totalAmount;
    private int inputWeight = 0;
    private int inputQuantity = 0;
    private int distanceCost = 0;
    int radioID1, radioID2;
    private Spinner districtSpinner;
    private Spinner districtSpinner2;
    TextView calcuTotal;
    RadioGroup paymentMethodGroup, paymentAtGroup;
    RadioButton paymentAtText,paymentMethodText;
    ArrayList<String> districts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        weightText = findViewById(R.id.weightText);
        productNameText = findViewById(R.id.productNameText);
        quantityText = findViewById(R.id.quantityText);
        receiverPhone = findViewById(R.id.receiveID);
        descriptionText = findViewById(R.id.descriptionText);
        userIDText = findViewById(R.id.userIDText);
        calcuTotal = findViewById(R.id.totalAmout);
        paymentMethodGroup = findViewById(R.id.paymentMethod);
        paymentAtGroup = findViewById(R.id.paymentAt);

         districtSpinner = findViewById(R.id.spinner1);
         districtSpinner2 = findViewById(R.id.spinner2);

        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(Payments.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);
        districtSpinner2.setAdapter(districtAdapter);

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromText = adapterView.getItemAtPosition(i).toString();
                if(fromText.length() != 0 && toText.length() != 0)
                getDistance();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        districtSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toText = adapterView.getItemAtPosition(i).toString();
                if(fromText.length() != 0 && toText.length() != 0)
                getDistance();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        quantityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0){
                    inputQuantity = Integer.parseInt(charSequence.toString());
                    totalCalculator();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        weightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0){
                    inputWeight = Integer.parseInt(charSequence.toString());
                    totalCalculator();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public void totalCalculator(){
        int total =0;
        if(inputQuantity != 0 && inputWeight != 0 && distanceCost != 0) {
             total = (inputQuantity * inputWeight * 500) + distanceCost;
        }
        totalAmount = String.valueOf(total);
        Log.e("Total = ", String.valueOf(totalAmount));
        calcuTotal.setText("Total Amount is K"+totalAmount);
    }
    public void getDistance(){
        Log.e("distacefrom",fromText);
            Log.e("distace",toText);
            districts = new ArrayList<>();
            districts.add("Mzuzu");
            districts.add("Nkhotakota");
            districts.add("Kasungu");
            districts.add("Salima");
            districts.add("Lilongwe");
            districts.add("Mangochi");
            districts.add("Blantyre");
            int to = 0;
            int from = 0;
            int distance = 0;
            for (int i = 1; i < districts.size(); i++) {
                if (districts.get(i).equals(toText)) {
                    to = i;
                }
                if (districts.get(i).equals(fromText)) {
                    from = i;
                }
            }
            if (to > from) {
                distance = to - from;
            } else {
                distance = from - to;
            }

            if(distance != 0)
            distanceCost = distance*300;
            else
                distanceCost = 200;

        Log.e("distace", String.valueOf(distanceCost));
            totalCalculator();

    }
    public void submitPayment(View view){
        radioID1 = paymentMethodGroup.getCheckedRadioButtonId();
        radioID2 = paymentAtGroup.getCheckedRadioButtonId();
        paymentAtText = findViewById(radioID2);
        paymentMethodText = findViewById(radioID1);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://"+ip+"/php_api/api/post/create.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("onResponse", response);
                        Log.e("statusCode", String.valueOf(mStatusCode));
                        if(mStatusCode == 200)
                        {
                            Toast.makeText(getBaseContext(), "Successfully Submitted ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Payments.this, HomeActivity.class));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Failling to send to api", String.valueOf(error));
                Toast.makeText(getBaseContext(), "Fail to submit", Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams(){
                Map<String, String> paramVal = new HashMap<>();
                paramVal.put("from",fromText);
                paramVal.put("to",toText);
                paramVal.put("weight",weightText.getText().toString());
                paramVal.put("name",productNameText.getText().toString());
                paramVal.put("quantity",quantityText.getText().toString());
                paramVal.put("description",descriptionText.getText().toString());
                paramVal.put("userID", userIDText.getText().toString());
                paramVal.put("paymentAt", paymentAtText.getText().toString());
                paramVal.put("paymentMethod", paymentMethodText.getText().toString());
                paramVal.put("amount", totalAmount);
                paramVal.put("receiver_phone", receiverPhone.getText().toString());
                paramVal.put("sender_phone", Contact);
                return  paramVal;
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