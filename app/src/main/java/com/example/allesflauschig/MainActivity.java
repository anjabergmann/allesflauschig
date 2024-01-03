package com.example.allesflauschig;


import static com.example.allesflauschig.utils.AllesFlauschigConstants.KEY_TEXT_REPLY;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.NOTIFICATION_CHANNEL_ID;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.example.allesflauschig.Exception.CrashHandler;
import com.example.allesflauschig.utils.AllesFlauschigConstants.Extras;
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

    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this.
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void createNotification() {

        CharSequence[] options = { "-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5" };

        String replyLabel = "Wie geht's? (-5 mies, 0 mittel, 5 gut)";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .setAllowFreeFormInput(true)
                //.setChoices(options)
                .build();

        Intent someIntent = new Intent(this, MyReceiver.class);
        someIntent.setAction("com.example.broadcast.MY_NOTIFICATION");
        //someIntent.putExtra("data", "AAAAAAAANothing to see here, move along.");
        someIntent.putExtra("blub", "AAAAAAAANothing to see here, move along.");
        someIntent.putExtra(Extras.NOTIFICATION_ID, "13");

        //sendBroadcast(someIntent);

        // Build a PendingIntent for the reply action to trigger.
        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(),
                        13, someIntent, PendingIntent.FLAG_MUTABLE);

        // Create the reply action and add the remote input.
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_stat_onesignal_default,
                        "Wie geht's?", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle("Alles flauschig?")
                //.setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle()
                        //.bigText("Much longer text that cannot fit one line... lalalala ich bin ein Keks jajajaja Kekse Kekske Kekse yummie yum"))
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(replyPendingIntent)
                //.setOngoing(true)
                .setAutoCancel(true)
                .addAction(action);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.notify(13, builder.build());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

    }
}