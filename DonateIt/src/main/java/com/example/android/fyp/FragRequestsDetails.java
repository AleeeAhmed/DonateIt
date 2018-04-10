package com.example.android.fyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragRequestsDetails extends AppCompatActivity {

    String id, title, desc, progress, target;
    TextView tvTitle, tvDesc, tvProgress, tvTarget;
    EditText edOfferAmount;
    Button btnSendOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_requests_details);

        intitalizations();

        id = getIntent().getStringExtra("ID");
        progress = getIntent().getStringExtra("Progress");
        target = getIntent().getStringExtra("Target");

        tvTitle.setText(getIntent().getStringExtra("Title"));
        tvDesc.setText(getIntent().getStringExtra("Desc"));
        tvProgress.setText(progress);
        tvTarget.setText(target);

        btnSendOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ofrAmount = Integer.parseInt(edOfferAmount.getText().toString());
                if (ofrAmount > (Integer.parseInt(target) - Integer.parseInt(progress))) {
                    Toast.makeText(FragRequestsDetails.this, "we only need "
                            + (Integer.parseInt(target) - Integer.parseInt(progress)) + " more.", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

    }

    private void intitalizations() {
        tvTitle = findViewById(R.id.tvTitle_RequestDetails);
        tvDesc = findViewById(R.id.tvDesc_RequestDetails);
        tvProgress = findViewById(R.id.tvProgress_RequestDetails);
        tvTarget = findViewById(R.id.tvTarget_RequestDetails);

        edOfferAmount = findViewById(R.id.tvOfferAmount_RequestDetails);
        btnSendOffer = findViewById(R.id.btnSendOffer);
    }
}
