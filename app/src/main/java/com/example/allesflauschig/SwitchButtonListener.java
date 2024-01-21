package com.example.allesflauschig;

import static com.example.allesflauschig.utils.AllesFlauschigConstants.Extras.CLICKED_BUTTON;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.Extras.CLICKED_BUTTON_OKAY;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.Extras.MOOD;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.Paths.MOOD_FILE;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.getBaseDirectory;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.allesflauschig.utils.AllesFlauschigConstants;
import com.example.allesflauschig.utils.CsvUtils;
import com.example.allesflauschig.utils.NotificationUtils;

import java.time.Instant;
import java.util.logging.Logger;

public class SwitchButtonListener extends BroadcastReceiver {
    Logger LOG = Logger.getLogger(this.getClass().getName());

    @Override
    public void onReceive(Context context, Intent intent) {
        LOG.info("I am here");

        String clickedButton = intent.getStringExtra(CLICKED_BUTTON);
        int mood = intent.getIntExtra(MOOD, 99);

        if (CLICKED_BUTTON_OKAY.equals(clickedButton) && mood == 99) {
            LOG.info("No number selected!");
            updateNotification(context, intent, null, "You gotta click on a Zahl first!");
        } else if (CLICKED_BUTTON_OKAY.equals(clickedButton)) {
            LOG.info("Number selected, closing notification.");
            CsvUtils.addEntry(getBaseDirectory(context), MOOD_FILE, new String[]{Instant.now().toString(), String.valueOf(mood)});
            cancelNotification(context, intent);
        } else {
            updatePreviousButtonValue(context, mood);
            updateNotification(context, intent, mood,null);
        }

        LOG.info("Answered with: " + getPreviousButtonValue(context));

    }

    private int getPreviousButtonValue(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ButtonPreferences", Context.MODE_PRIVATE);
        return preferences.getInt("previousButtonValue", 99);
    }

    private void updatePreviousButtonValue(Context context, int buttonValue) {
        SharedPreferences preferences = context.getSharedPreferences("ButtonPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("previousButtonValue", buttonValue);
        editor.apply();
    }

    private void updateNotification(Context context, Intent intent, Integer mood, String infoText) {
        int notificationId = intent.getIntExtra(AllesFlauschigConstants.Extras.NOTIFICATION_ID, 0);

        LOG.info(String.format("Updating notification with id '%s'", notificationId));
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(notificationId, NotificationUtils.createNotificationUpdate(context, notificationId, mood, infoText));
    }

    private void cancelNotification(Context context, Intent intent) {
        int notificationId = intent.getIntExtra(AllesFlauschigConstants.Extras.NOTIFICATION_ID, 0);

        LOG.info(String.format("Cancelling notification with id '%s'", notificationId));
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.cancel(notificationId);
    }
}