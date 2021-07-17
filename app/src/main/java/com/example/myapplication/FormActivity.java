package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class FormActivity extends AppCompatActivity {

    private String form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_data);

        TextView formTextView = (TextView) findViewById(R.id.formTextView);

        Intent intent = getIntent();
        form = intent.getStringExtra(InvoiceActivity.EXTRA_FORM);


        formTextView.setText(form);
    }
}