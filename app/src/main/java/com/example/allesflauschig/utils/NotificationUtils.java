package com.example.allesflauschig.utils;

import static com.example.allesflauschig.utils.AllesFlauschigConstants.NOTIFICATION_CHANNEL_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import com.example.allesflauschig.R;

public class NotificationUtils {

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

}
