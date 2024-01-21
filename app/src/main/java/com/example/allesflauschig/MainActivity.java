package com.example.allesflauschig;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.allesflauschig.Exception.CrashHandler;
import com.example.allesflauschig.utils.NotificationUtils;

import java.util.Calendar;
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

        scheduleNotification();
    }

    public void scheduleNotification() {
        final JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        // The JobService that we want to run
        final ComponentName name = new ComponentName(this, NotificationService.class);

        // Schedule the job
        final int result = jobScheduler.schedule(
                new JobInfo.Builder(1, name)
                        //.setPeriodic(24 * 60 * 60 * 1000)
                        .setPeriodic(8 * 60 * 60 * 1000)
                        .build());

        // If successfully scheduled, log this thing
        if (result == JobScheduler.RESULT_SUCCESS) {
            LOG.info( "Scheduled job successfully!");
        }
    }


    public void scheduleNotification_template(Calendar calendar) {
        Intent intent = new Intent(getApplicationContext(), Notification.class);
        intent.putExtra("titleExtra", "Dynamic Title");
        intent.putExtra("textExtra", "Dynamic Text Body");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(getApplicationContext(), "Scheduled ", Toast.LENGTH_LONG).show();
    }

    private void goToHistory() {
        Intent myIntent = new Intent(this, HistoryActivity.class);
        this.startActivity(myIntent);
    }

    private void createNotification() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Integer notificationId = NotificationUtils.getNotificationId();
            notificationManager.notify(notificationId, NotificationUtils.createNotification(this, notificationId));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

    }
}