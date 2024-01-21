package com.example.allesflauschig;

import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.allesflauschig.utils.NotificationUtils;

import java.util.logging.Logger;


public class NotificationService extends JobService {

    Logger LOG = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean onStartJob(JobParameters params) {
        createNotification();
        return false;
    }

    private void createNotification() {
        LOG.info("NotificationService is creating notification");
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        int notificationId = NotificationUtils.getNotificationId();
        notificationManager.notify(notificationId, NotificationUtils.createNotification(this, notificationId));
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
