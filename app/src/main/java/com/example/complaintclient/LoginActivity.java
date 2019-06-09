package com.example.complaintclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintclient.api.ComplaintService;
import com.example.complaintclient.models.User;
import com.example.complaintclient.network.ServiceGenerator;
import com.example.complaintclient.utils.SessionManager;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputUsername, textInputPassword;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(LoginActivity.this);

        textInputUsername = findViewById(R.id.text_layout_login_username);
        textInputPassword = findViewById(R.id.text_layout_login_password);

        Button login = findViewById(R.id.btn_login);

        login.setOnClickListener(v -> {
            String username = textInputUsername.getEditText().getText().toString();
            String password = textInputPassword.getEditText().getText().toString();

            if (username.isEmpty()) {
                textInputUsername.setErrorEnabled(true);
                textInputUsername.setError("username tidak boleh kosong");
            }
            if (password.isEmpty()) {
                textInputPassword.setErrorEnabled(true);
                textInputPassword.setError("password tidak boleh kosong");
            }

            if (!username.isEmpty() && !password.isEmpty()) {
                signIn(username, password);
            }
        });

        TextView toRegister = findViewById(R.id.text_toRegister);

        toRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }

    private void signIn(final String username, String password) {

        ComplaintService service = ServiceGenerator.createService(ComplaintService.class, username, password);

        User user = new User(
                username,
                password
        );

        Call<User> call = service.login(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    sessionManager.storeToken(Credentials.basic(username, password));

                    generateUserDetails(response.body());

                    Toast.makeText(LoginActivity.this, "LoginActivity success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    if (response.code() == 401) {
                        Toast.makeText(LoginActivity.this, "Username or Password not valid", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void generateUserDetails(User user) {

        if (user.getRole().getName().equals("ROLE_ADMIN_INST")) {
            sessionManager.createSession(
                    user.getUsername(),
                    user.getRole().getName(),
                    user.getInstance().getName()
            );
        } else {
            sessionManager.createSession(
                    user.getUsername(),
                    user.getRole().getName(),
                    null
            );
        }
    }
}
