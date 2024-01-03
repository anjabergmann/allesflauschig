package com.example.allesflauschig;

import static com.example.allesflauschig.utils.AllesFlauschigConstants.KEY_TEXT_REPLY;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.RemoteInput;

import com.example.allesflauschig.utils.AllesFlauschigConstants.Extras;
import com.example.allesflauschig.utils.CsvUtils;

import org.apache.commons.lang3.math.NumberUtils;

import java.time.Instant;
import java.util.Objects;
import java.util.logging.Logger;

public class MyReceiver extends BroadcastReceiver {

    Logger LOG = Logger.getLogger(this.getClass().getName());

    @Override
    public void onReceive(Context context, Intent intent) {
        String answer = getMessageText(intent);
        LOG.info(String.format("Notification answered with: '%s'", answer));
        // todo: validate entry?
        CsvUtils.addEntry(Objects.requireNonNull(context.getDataDir()).getAbsolutePath(), "mood.csv", new String[]{Instant.now().toString(), answer});
        cancelNotification(context, intent);
    }

    private String getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return String.valueOf(remoteInput.getCharSequence(KEY_TEXT_REPLY));
        }
        return null;
    }

    private void cancelNotification(Context context, Intent intent) {
        String notificationIdString = intent.getStringExtra(Extras.NOTIFICATION_ID);

        if (NumberUtils.isDigits(notificationIdString)) {
            LOG.info(String.format("Cancelling notification with id '%s'", notificationIdString));
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.cancel(Integer.parseInt(notificationIdString));
        } else {
            LOG.info(String.format("Unable to cancel notificaion with id '%s'", notificationIdString));
        }
    }
}