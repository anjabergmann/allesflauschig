package com.example.allesflauschig;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.allesflauschig.Exception.CrashHandler;
import com.example.allesflauschig.utils.NotificationUtils;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    Logger LOG = Logger.getLogger(this.getClass().getName());

    Button button_notify;
    Button button_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOG.info("Started activity MainActivity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));

        NotificationUtils.createNotificationChannel(this);

        button_notify = findViewById(R.id.button_notify);
        button_notify.setOnClickListener(v -> createNotification());

        button_history = findViewById(R.id.button_history);
        button_history.setOnClickListener(v -> goToHistory());
    }

    private void goToHistory() {
        Intent myIntent = new Intent(this, HistoryActivity.class);
        this.startActivity(myIntent);
    }

    private void createNotification() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.notify(13, NotificationUtils.createNotification(this));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

    }
}