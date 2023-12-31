package com.example.allesflauschig;

import static com.example.allesflauschig.MainActivity.KEY_TEXT_REPLY;
import static com.example.allesflauschig.utils.AllesFlauschigConstants.EXTRAS_NOTIFICATION_ID;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.RemoteInput;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.logging.Logger;

public class MyReceiver extends BroadcastReceiver {

    Logger LOG = Logger.getLogger(this.getClass().getName());

    @Override
    public void onReceive(Context context, Intent intent) {
        LOG.info(String.format("Notification answered with: '%s'", getMessageText(intent)));
        // todo: validate(?) and store reply
        cancelNotification(context, intent);
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_TEXT_REPLY);
        }
        return null;
    }

    private void cancelNotification(Context context, Intent intent) {
        String notificationIdString = intent.getStringExtra(EXTRAS_NOTIFICATION_ID);

        if (NumberUtils.isDigits(notificationIdString)) {
            LOG.info(String.format("Cancelling notification with id '%s'", notificationIdString));
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.cancel(Integer.parseInt(notificationIdString));
        } else {
            LOG.info(String.format("Unable to cancel notificaion with id '%s'", notificationIdString));
        }
    }
}