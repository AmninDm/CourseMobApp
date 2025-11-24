package com.example.coursapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword, etPasswordConfirm;
    private Button btnRegister;
    private TextView tvToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        tvToLogin = findViewById(R.id.tvToLogin);

        btnRegister.setOnClickListener(v -> performRegister());

        tvToLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void performRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();
        String passConfirm = etPasswordConfirm.getText().toString();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Введите имя");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Введите email");
            return;
        }
        if (pass.length() < 6) {
            etPassword.setError("Минимум 6 символов");
            return;
        }
        if (!pass.equals(passConfirm)) {
            etPasswordConfirm.setError("Пароли не совпадают");
            return;
        }

        Toast.makeText(this, "Регистрация успешна!\n" + name, Toast.LENGTH_LONG).show();

        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}