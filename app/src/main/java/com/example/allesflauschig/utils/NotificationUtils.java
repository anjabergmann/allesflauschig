package com.example.allesflauschig.utils;

import static com.example.allesflauschig.utils.AllesFlauschigConstants.KEY_TEXT_REPLY;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.NOTIFICATION_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.example.allesflauschig.MyReceiver;
import com.example.allesflauschig.R;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationUtils {

    private final static AtomicInteger notificationId = new AtomicInteger(0);

    public static int getNotificationId() {
        return notificationId.incrementAndGet();
    }

    public static void createNotificationChannel(Context context) {
        CharSequence name = context.getString(R.string.channel_name);
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this.
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public static Notification createNotification(Context context, Integer notificationId) {

        CharSequence[] options = { "-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5" };

        String replyLabel = "Wie geht's? (-5 mies, 0 mittel, 5 gut)";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .setAllowFreeFormInput(true)
                //.setChoices(options)
                .build();

        Intent someIntent = new Intent(context, MyReceiver.class);
        someIntent.setAction("com.example.broadcast.MY_NOTIFICATION");
        someIntent.putExtra("data", "Nothing to see here, move along.");
        someIntent.putExtra(AllesFlauschigConstants.Extras.NOTIFICATION_ID, notificationId);

        //sendBroadcast(someIntent);

        // Build a PendingIntent for the reply action to trigger.
        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        getNotificationId(), someIntent, PendingIntent.FLAG_MUTABLE);

        // Create the reply action and add the remote input.
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_stat_onesignal_default,
                        "Wie geht's?", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), NOTIFICATION_CHANNEL_ID)
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

        return builder.build();
    }

}
