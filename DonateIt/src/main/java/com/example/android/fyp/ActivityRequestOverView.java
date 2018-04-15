package com.example.android.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityRequestOverView extends AppCompatActivity {

    EditText edTitle, edDesc;
    Button btnNext;
    String title, desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_overview);

        edTitle = findViewById(R.id.etTitleRequestOverview);
        edDesc = findViewById(R.id.etDescriptionRequestOverview);
        btnNext = findViewById(R.id.btnNextRequestOverview);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = edTitle.getText().toString().trim();
                desc = edDesc.getText().toString().trim();
                if (title.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(ActivityRequestOverView.this, "Fill above fields..", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ActivityRequestOverView.this, ActivityPostRequest.class);
                    intent.putExtra("Title", title);
                    intent.putExtra("Desc", desc);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
