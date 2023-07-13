package com.sexed.androidapp.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.foysaltech.wptoandroidapp.R;
import com.sexed.androidapp.adapter.PostAdapter;
import com.sexed.androidapp.model.Blog;
import com.sexed.androidapp.model.Post;
import com.sexed.androidapp.util.InternetConnection;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView postList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Blog> postItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        postList = findViewById(R.id.postRecycler);
        swipeRefreshLayout = findViewById(R.id.parentLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                Log.d("Swipe", "Refreshing");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        setListContent(false);
                    }
                }, 3000);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        setListContent(true);

    }

    public void setListContent(final boolean withProgress) {

        if (InternetConnection.checkInternetConnection(getApplicationContext())) {


            ApiService api = WordPressClient.getApiService();
            Call<List<Blog>> call = api.getPosts();

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle(getString(R.string.progressdialog_title));
            progressDialog.setMessage(getString(R.string.progressdialog_message));

            if(withProgress) {
                progressDialog.show();
            }

            call.enqueue(new Callback<List<Blog>>() {
                @Override
                public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                    Log.d("RetrofitResponse", "Status Code " + response.code());
                    postItemList = response.body();
                    postList.setHasFixedSize(true);
                    postList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    postList.setAdapter(new PostAdapter(getApplicationContext(), postItemList));

                    if(withProgress) {
                        progressDialog.dismiss();
                    }


                }

                @Override
                public void onFailure(Call<List<Blog>> call, Throwable t) {
                    Log.d("RetrofitResponse", "Error");
                    if(withProgress) {
                        progressDialog.dismiss();
                    }
                }
            });


        } else {
            Snackbar.make(swipeRefreshLayout, "No se puede conectar a internet", Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

   ;


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}