package com.example.complaintclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.complaintclient.api.ComplaintService;
import com.example.complaintclient.models.User;
import com.example.complaintclient.network.ServiceGenerator;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextInputLayout textInputName = findViewById(R.id.text_layout_register_name);
        TextInputLayout textInputEmail = findViewById(R.id.text_layout_register_email);
        TextInputLayout textInputUsername = findViewById(R.id.text_layout_register_username);
        TextInputLayout textInputPassword = findViewById(R.id.text_layout_register_password);
        TextInputLayout textInputPasswordConfirm = findViewById(R.id.text_layout_register_passwordConfirm);
        Button buttonRegister = findViewById(R.id.btn_register);

        buttonRegister.setOnClickListener(v -> {
            String password = textInputPassword.getEditText().getText().toString();
            String passwordConfirm = textInputPasswordConfirm.getEditText().getText().toString();
            String name = textInputName.getEditText().getText().toString();
            String email = textInputEmail.getEditText().getText().toString();
            String username = textInputUsername.getEditText().getText().toString();
            if (!name.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty()) {
                if (!password.equals(passwordConfirm)) {
                    textInputPassword.setErrorEnabled(true);
                    textInputPassword.setError("password tidak sama");

                    textInputPasswordConfirm.setErrorEnabled(true);
                    textInputPasswordConfirm.setError("password tidak sama");
                } else {
                    textInputPassword.setError(null);
                    textInputPasswordConfirm.setError(null);

                    textInputPassword.setErrorEnabled(false);
                    textInputPasswordConfirm.setErrorEnabled(false);
                    new registerUser(name, email, username, password);
                }
            } else {
                Toast.makeText(RegisterActivity.this, "form tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class registerUser {
        registerUser(String name, String email, String username, String password) {

            ComplaintService service = ServiceGenerator.createService(ComplaintService.class);

            User user = new User(
                    name,
                    username,
                    email,
                    password
            );

            Call<ResponseBody> call = service.register(user);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            Toast.makeText(RegisterActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Toast.makeText(RegisterActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("ERROR", t.getMessage());
                }
            });

        }
    }
}
