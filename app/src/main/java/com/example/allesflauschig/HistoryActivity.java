package com.example.allesflauschig;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allesflauschig.utils.CsvUtils;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class HistoryActivity extends AppCompatActivity {

    Logger LOG = Logger.getLogger(this.getClass().getName());

    Button button_backToMain;
    TextView text_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOG.info("Start activity AnotherActivity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        text_history = findViewById(R.id.text_history);
        List<String[]> history = CsvUtils.readFromFile(getDataDir().getAbsolutePath(), "mood.csv");
        history.forEach(entry -> text_history.append(Arrays.toString(entry)));

        button_backToMain = findViewById(R.id.button_backToMain);
        button_backToMain.setOnClickListener(v -> backToMain());

    }

    private void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
