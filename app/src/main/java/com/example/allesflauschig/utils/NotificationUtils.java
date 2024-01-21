package com.example.allesflauschig.utils;

import static com.example.allesflauschig.utils.AllesFlauschigConstants.Extras.CLICKED_BUTTON;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.Extras.CLICKED_BUTTON_OKAY;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.Extras.MOOD;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.Extras.NOTIFICATION_ID;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.NOTIFICATION_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.allesflauschig.R;
import com.example.allesflauschig.SwitchButtonListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationUtils {

    private static final Random RANDOM = new Random();
    private final static AtomicInteger notificationIdAtomicInt = new AtomicInteger(RANDOM.nextInt());

    private final static Map<Integer, Integer> VALUEL_TO_BUTTON_MAP = new HashMap<>() {{
        put(5, R.id.button_five);
        put(4, R.id.button_four);
        put(3, R.id.button_three);
        put(2, R.id.button_two);
        put(1, R.id.button_one);
        put(0, R.id.button_zero);
        put(-1, R.id.button_onebelow);
        put(-2, R.id.button_twobelow);
        put(-3, R.id.button_threebelow);
        put(-4, R.id.button_fourbelow);
        put(-5, R.id.button_fivebelow);
    }};

    private final static Map<Integer, Integer> BUTTON_TO_VALUE_MAP = new HashMap<>() {{
        put(R.id.button_five, 5);
        put(R.id.button_four, 4);
        put(R.id.button_three, 3);
        put(R.id.button_two, 2);
        put(R.id.button_one, 1);
        put(R.id.button_zero, 0);
        put(R.id.button_onebelow, -1);
        put(R.id.button_twobelow, -2);
        put(R.id.button_threebelow, -3);
        put(R.id.button_fourbelow, -4);
        put(R.id.button_fivebelow, -5);
    }};

    public static int getNotificationId() {
        return notificationIdAtomicInt.incrementAndGet();
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

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_mood);

        NotificationCompat.Builder builder = createNotificationBuilder(context, remoteViews, null, notificationId);

        return builder.build();
    }

    public static Notification createNotificationUpdate(Context context, Integer notificationId, Integer mood, String infoText) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_mood);

        // Update the design of the clicked button
        Integer clickedButton = VALUEL_TO_BUTTON_MAP.get(mood);
        if (clickedButton != null) {
            remoteViews.setInt(clickedButton, "setBackgroundColor", ContextCompat.getColor(context, R.color.black));
            remoteViews.setInt(clickedButton, "setTextColor", ContextCompat.getColor(context, R.color.white));
        }

        if (infoText != null) {
            remoteViews.setTextViewText(R.id.info_text, infoText);
        }

        NotificationCompat.Builder builder = createNotificationBuilder(context, remoteViews, mood, notificationId);
        builder.setSilent(true); // do not make sound if the notification is updated

        return builder.build();
    }

    private static NotificationCompat.Builder createNotificationBuilder(Context context, RemoteViews remoteViews, Integer mood, int notificationId) {
        BUTTON_TO_VALUE_MAP.forEach((button, value) -> {
            Intent intent = new Intent(context, SwitchButtonListener.class);
            intent.putExtra(NOTIFICATION_ID, notificationId);
            intent.putExtra(MOOD, value);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, getNotificationId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            remoteViews.setOnClickPendingIntent(button, pendingIntent);
        });

        Intent okayIntent = new Intent(context, SwitchButtonListener.class);
        okayIntent.putExtra(CLICKED_BUTTON, CLICKED_BUTTON_OKAY);
        okayIntent.putExtra(NOTIFICATION_ID, notificationId);
        if (mood != null) {
            okayIntent.putExtra(MOOD, mood);
        }
        PendingIntent okayPendingIntent = PendingIntent.getBroadcast(context, getNotificationId(), okayIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        remoteViews.setOnClickPendingIntent(R.id.button_notify, okayPendingIntent);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle("Alles flauschig?")
                .setAutoCancel(true)
                .setCustomBigContentView(remoteViews);

        return builder;
    }

}
