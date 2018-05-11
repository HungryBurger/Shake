package org.androidtown.shaketest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class MainUIActivity extends AppCompatActivity {

    private ToggleButton service_btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);

        service_btn = findViewById(R.id.register_service_btn);
        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShakeService.class);
                if (service_btn.isChecked()) {
                    startService(intent);
                } else {
                    stopService(intent);
                }
            }
        });
    }
}
