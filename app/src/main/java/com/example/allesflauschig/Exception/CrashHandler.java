package com.example.allesflauschig.Exception;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.logging.Logger;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    Logger LOG = Logger.getLogger(this.getClass().getName());

    private String newLine = "\n";
    private StringBuilder errorMessage = new StringBuilder();
    private StringBuilder softwareInfo = new StringBuilder();
    private StringBuilder dateInfo = new StringBuilder();
    private Context context;

    public CrashHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        LOG.severe("Uncaught exception!");

        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));

        errorMessage.append(stackTrace);

        softwareInfo.append("SDK: ");
        softwareInfo.append(Build.VERSION.SDK_INT);
        softwareInfo.append(newLine);
        softwareInfo.append("Release: ");
        softwareInfo.append(Build.VERSION.RELEASE);
        softwareInfo.append(newLine);
        softwareInfo.append("Incremental: ");
        softwareInfo.append(Build.VERSION.INCREMENTAL);
        softwareInfo.append(newLine);

        dateInfo.append(Calendar.getInstance().getTime());
        dateInfo.append(newLine);

        Log.d("Error" , errorMessage.toString());
        Log.d("Software" , softwareInfo.toString());
        Log.d("Date" , dateInfo.toString());


        Intent intent = new Intent(context, CrashActivity.class);
        intent.putExtra("Error" , errorMessage.toString());
        intent.putExtra("Software" , softwareInfo.toString());
        intent.putExtra("Date" , dateInfo.toString());

        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(2);

    }

}



