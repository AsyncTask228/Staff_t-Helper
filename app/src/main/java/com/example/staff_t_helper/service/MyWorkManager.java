package com.example.staff_t_helper.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.staff_t_helper.MainActivity;
import com.example.staff_t_helper.R;
import com.example.staff_t_helper.nodb.NoDb;
import com.example.staff_t_helper.rest.LibraryApiVolley;

public class MyWorkManager extends Worker {

    private Context context;
    private WorkerParameters workerParams;
    private boolean threadStatus;
    private LibraryApiVolley libraryApiVolley;

    private int getCount = NoDb.PROBLEM_LIST.size();

    private NotificationManager notificationManager;
    private static final String  CHANNEL_ID = "CHANNEL_ID";

    public MyWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.workerParams = workerParams;

        libraryApiVolley = new LibraryApiVolley(getApplicationContext());  //getApplicationContext();
        threadStatus = true;
    }

    public void request() {
            while (threadStatus) {
                Log.i("MY_TAG", "new_request");
                  try {
                      Thread.sleep(1000);
                      libraryApiVolley.fillProblem();
                      //создание уведомления
                      if (getCount != NoDb.PROBLEM_LIST.size()) {
                          notificationManager = (NotificationManager) context.getSystemService(getApplicationContext().NOTIFICATION_SERVICE);  //getApplicationContext();
                          Intent intent = new Intent(context, MainActivity.class);       //getApplicationContext();
                          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                          PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);  //getApplicationContext();
                          NotificationCompat.Builder notificationBuilder =
                                  new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                                          .setAutoCancel(false)
                                          .setSmallIcon(R.mipmap.ic_launcher)
                                          .setWhen(System.currentTimeMillis())
                                          .setContentIntent(pendingIntent)
                                          .setContentTitle("ТРЕБУЕТСЯ ПОМОЩЬ")
                                          .setContentText("Срочно окажите помощь")
                                          .setPriority(1);
                          createChannelIfNeeded(notificationManager);
                          notificationManager.notify(1, notificationBuilder.build());
                          getCount = NoDb.PROBLEM_LIST.size();
                      }
                  }catch (Exception e){

                  }

            }
        }

    private void createChannelIfNeeded(NotificationManager notificationManager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("MY_TAG", "doWorkOver");
        request();
        return Result.success();
    }
}