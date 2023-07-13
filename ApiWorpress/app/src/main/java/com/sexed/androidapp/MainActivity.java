package com.sexed.androidapp;

import static com.foysaltech.wptoandroidapp.R.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.foysaltech.wptoandroidapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sexed.androidapp.adapter.PostAdapter;
import com.sexed.androidapp.app.ApiService;
import com.sexed.androidapp.app.WordPressClient;
import com.sexed.androidapp.model.Post;
import com.sexed.androidapp.util.InternetConnection;

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
    private List<Post> postItemList;

    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.navigation_drawer);

        toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(id.drawer_layout);
        navigationView = findViewById(id.navigation_view);

        postList = findViewById(id.postRecycler);
        swipeRefreshLayout = findViewById(id.parentLayout);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        if (user==null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        /* //TEST
        else {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        }*/

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
                drawerLayout, toolbar, string.open_drawer, string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setListContent(true);



        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent miintent=new Intent(MainActivity.this,MainActivityBot.class);
                startActivity(miintent);


            }


        });


        //desde aca

        //setContentView(R.layout.activity_main); //ojito con este si no comento muestra vista en blanco




        //hsat aca




    /* protected void nuevaf () {

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent miintent=new Intent(MainActivity.this,MainActivityBot.class);
                startActivity(miintent);




            }


        });


    }

    */


    }

    public void setListContent(final boolean withProgress) {

        if (InternetConnection.checkInternetConnection(getApplicationContext())) {


            ApiService api = WordPressClient.getApiService();
            Call<List<Post>> call = api.getPosts();

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle(getString(string.progressdialog_title));
            progressDialog.setMessage(getString(string.progressdialog_message));

            if(withProgress) {
                progressDialog.show();
            }

            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
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
                public void onFailure(Call<List<Post>> call, Throwable t) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.log_out) {
            Toast.makeText(getApplicationContext(), "Out test", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}