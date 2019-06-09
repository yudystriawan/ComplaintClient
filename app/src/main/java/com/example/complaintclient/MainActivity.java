package com.example.complaintclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.complaintclient.api.ComplaintService;
import com.example.complaintclient.models.User;
import com.example.complaintclient.network.ServiceGenerator;
import com.example.complaintclient.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;
    private TextView vUserName, vEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initNavHeader(navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void initNavHeader(NavigationView navigationView) {
        View navHeaderView = navigationView.getHeaderView(0);
        vUserName = navHeaderView.findViewById(R.id.nav_header_user_name);
        vEmail = navHeaderView.findViewById(R.id.nav_header_email);
        sessionManager = new SessionManager(MainActivity.this);
        setNavHeaderData();
    }

    private void setNavHeaderData() {
        String token = sessionManager.getToken();

        if (token != null) {
            getDataUser(token);
        } else {
            vUserName.setText("GUEST");
            vUserName.setPadding(0, 0, 0, 8);
            vEmail.setVisibility(View.GONE);
        }
    }

    private void getDataUser(String token) {
        String username = sessionManager.getUsername();
        ComplaintService service = ServiceGenerator.createService(ComplaintService.class, token);
        Call<User> call = service.getUser(username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    generateNavHeaderData(user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }

    private void generateNavHeaderData(User user) {
        if (user.getRole().getName().equals("ROLE_USER")) {
            vEmail.setText(user.getEmail());
        }else if (user.getRole().getName().equals("ROLE_ADMIN_INST")){
            vEmail.setText(user.getInstance().getName());
        }else{
            vEmail.setVisibility(View.GONE);
        }

        vUserName.setText(user.getName());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
                break;
            case R.id.nav_setting:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new SettingFragment())
                        .commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
