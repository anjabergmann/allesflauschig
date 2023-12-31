package com.example.allesflauschig;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.logging.Logger;

public class AnotherActivity extends AppCompatActivity {

    Logger LOG = Logger.getLogger(this.getClass().getName());

    Button button_backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOG.info("AnotherActivity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        button_backToMain = findViewById(R.id.button_backToMain);
        button_backToMain.setOnClickListener(v -> backToMain());

    }

    private void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
