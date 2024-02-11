package com.example.allesflauschig;

import static com.example.allesflauschig.utils.AllesFlauschigConstants.Extras.CLICKED_BUTTON;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.Extras.CLICKED_BUTTON_OKAY;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.Extras.CLICKED_BUTTON_OPEN_APP;
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
        LOG.info("SwitchButtonListener received a notification interaction.");

        String clickedButton = intent.getStringExtra(CLICKED_BUTTON);
        int mood = intent.getIntExtra(MOOD, 99);

        LOG.info(String.format("Notification button '%s' was clicked. Currently selected value is '%s'", clickedButton, mood));

        if (CLICKED_BUTTON_OPEN_APP.equals(clickedButton) && mood == 99) {
            // simply cancel the notification and open the app on the screen
            LOG.info("'Open app' button was clicked without selecting a number. Cancelling notification.");
            cancelNotification(context, intent);
            Intent openApp = new Intent(context, MainActivity.class);
            openApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openApp);
        } else if (CLICKED_BUTTON_OPEN_APP.equals(clickedButton)) {
            // a number was entered before clicking the open app button, so we want to store that and then cancel the notification and open the app on the screen
            LOG.info("'Open app' button was clicked and a number was selected. Storing number and cancelling notification.");
            cancelNotification(context, intent);
            Intent openApp = new Intent(context, MainActivity.class);
            openApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openApp);
        } else if (CLICKED_BUTTON_OKAY.equals(clickedButton) && mood == 99) {
            // 'okay' button was clicked, but no number is selected - we do not accept that. Instead of cancelling the notification we add an info message.
            LOG.info("'Okay' button was clicked and no number was selected. Adding info message to notification.");
            updateNotification(context, intent, null, "You gotta click on a Zahl first!");
        } else if (CLICKED_BUTTON_OKAY.equals(clickedButton)) {
            // 'okay' button was clicked and a number was selected - we want to store the number and then cancel the notification
            LOG.info("'Okay' button was clicked with a number selected. Storing number and closing notification.");
            CsvUtils.addEntry(getBaseDirectory(context), MOOD_FILE, new String[]{Instant.now().toString(), String.valueOf(mood)});
            cancelNotification(context, intent);
        } else {
            // A button with a number was clicked. We want to store that number as an extra on the intent of the 'okay' and 'open app' buttons so we can store it if one of those is clicked. Also we will highlight the selected number in the notification.
            LOG.info("A number button was clicked. Updating the notification to highlight the selected number and adding it to intent extras for the 'Okay' and 'Open app' buttons so it can be stored if one of those buttons is clicked.");
            // updatePreviousButtonValue(context, mood);
            updateNotification(context, intent, mood,null);
        }
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