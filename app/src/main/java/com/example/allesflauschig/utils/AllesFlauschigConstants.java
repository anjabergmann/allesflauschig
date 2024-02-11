package com.example.allesflauschig.utils;

import android.content.Context;

public class AllesFlauschigConstants {

        public static final String NOTIFICATION_CHANNEL_ID = "allesFlauschigNotificationChannel";
        public static final String KEY_TEXT_REPLY = "mood_notification_reply";
        public static final String[] MOOD_FILE_HEADERS = new String[]{"timestamp", "mood"};

        public static String getBaseDirectory(Context context) {
                return context.getFilesDir().getAbsolutePath();
        }

        public static String getMoodFilePath(Context context) {
            return context.getFilesDir().getAbsolutePath() + Paths.MOOD_FILE;
        }

        public class Paths {
                public static final String MOOD_FILE = "mood.csv";
        }

        public class Extras {
                public static final String NOTIFICATION_ID = "notificationId";

                public static final String MOOD = "mood";
                public static final String CLICKED_BUTTON = "clickedButton";
                public static final String CLICKED_BUTTON_OKAY = "okayButton";
                public static final String CLICKED_BUTTON_OPEN_APP = "openAppButton";
        }

}
