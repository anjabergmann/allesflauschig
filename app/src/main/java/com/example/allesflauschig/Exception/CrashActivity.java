package com.example.allesflauschig.Exception;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allesflauschig.MainActivity;
import com.example.allesflauschig.R;

import java.util.logging.Logger;

public class CrashActivity extends AppCompatActivity {

    Logger LOG = Logger.getLogger(this.getClass().getName());

    Button button_backToMain;
    TextView text_error;
    TextView text_software;
    TextView text_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOG.info("Displaying CrashActivity.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        button_backToMain = findViewById(R.id.button_backToMainn);
        button_backToMain.setOnClickListener(v -> backToMain());

        Intent intent = getIntent();

        text_error = findViewById(R.id.text_error);
        text_error.setText(intent.getStringExtra("Error"));

        text_software = findViewById(R.id.text_software);
        text_software.setText(intent.getStringExtra("Software"));

        text_date = findViewById(R.id.text_date);
        text_date.setText(intent.getStringExtra("Date"));
    }

    private void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
