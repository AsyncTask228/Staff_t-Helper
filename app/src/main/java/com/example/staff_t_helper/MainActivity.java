package com.example.staff_t_helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.example.staff_t_helper.adapter.ProblemAdapter;
import com.example.staff_t_helper.domain.Person;
import com.example.staff_t_helper.domain.Problem;
import com.example.staff_t_helper.nodb.NoDb;
import com.example.staff_t_helper.rest.LibraryApiVolley;
import com.example.staff_t_helper.utillity.NetworkChangeListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvProblem;
    private ProblemAdapter problemAdapter;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private boolean threadStatus;

    private ItemTouchHelper.SimpleCallback simpleCallback;
    private LibraryApiVolley libraryApiVolley;

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

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    private class MyThread extends Thread {
        @Override
        public void run() {

            while (threadStatus) {
                try {
                    libraryApiVolley.fillProblem();
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        threadStatus = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadStatus = false;
    }
}

