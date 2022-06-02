package com.example.staff_t_helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.staff_t_helper.adapter.ProblemAdapter;
import com.example.staff_t_helper.domain.Problem;
import com.example.staff_t_helper.nodb.NoDb;
import com.example.staff_t_helper.rest.LibraryApiVolley;
import com.example.staff_t_helper.service.MyWorkManager;
import com.example.staff_t_helper.utillity.NetworkChangeListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvProblem;
    private ProblemAdapter problemAdapter;

    public NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private int getCount = NoDb.PROBLEM_LIST.size();

    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    private boolean threadStatus;

    private ItemTouchHelper.SimpleCallback simpleCallback;
    private LibraryApiVolley libraryApiVolley;

    private WorkManager workManager;
    private WorkRequest workRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        threadStatus = true;

        libraryApiVolley = new LibraryApiVolley(MainActivity.this);

        rvProblem = findViewById(R.id.rv_problems);

        problemAdapter = new ProblemAdapter(MainActivity.this, NoDb.PROBLEM_LIST);
        rvProblem.scrollToPosition(problemAdapter.getItemCount());
        rvProblem.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rvProblem.setAdapter(problemAdapter);


        Thread myThread = new MyThread();
        myThread.setDaemon(true);
        myThread.start();


        simpleCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                Problem problem = NoDb.PROBLEM_LIST.get(viewHolder.getAdapterPosition());
                Log.d("123", problem.toString());

                if (direction == ItemTouchHelper.LEFT) {

                    libraryApiVolley.deleteProblem(problem.getId());
                }

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvProblem);

    }

    public void updateAdapter() {

        problemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    public class MyThread extends Thread {

        @Override
        public void run() {
            request();
        }

        public void request() {
            while (threadStatus) {
                try {
                    libraryApiVolley.fillProblem();

                    //создание уведомления
                    if (getCount != NoDb.PROBLEM_LIST.size()) {
                        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder notificationBuilder =
                                new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                                        .setAutoCancel(false)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setWhen(System.currentTimeMillis())
                                        .setContentIntent(pendingIntent)
                                        .setContentTitle("ТРЕБУЕТСЯ ПОМОЩЬ")
                                        .setContentText("Срочно окажите помощь")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        createChannelIfNeeded(notificationManager);
                        notificationManager.notify(1, notificationBuilder.build());
                        getCount = NoDb.PROBLEM_LIST.size();
                    }

                    sleep(1 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createChannelIfNeeded(NotificationManager manager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MY_TAG", "onStop()");

        //создание фонового потока
        workManager = WorkManager.getInstance(getApplicationContext());
        workManager.enqueue(new OneTimeWorkRequest.Builder(MyWorkManager.class).build());
    }

    @Override
    protected void onDestroy() {

        Log.i("MY_TAG", "onDestroy()");
        super.onDestroy();
    }
}