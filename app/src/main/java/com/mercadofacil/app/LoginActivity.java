package com.mercadofacil.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_login);

        EditText edtUser = findViewById(R.id.edtUsuario);
        EditText edtPass = findViewById(R.id.edtSenha);
        Button btn = findViewById(R.id.btnLogin);

        btn.setOnClickListener(v -> {
            String u = edtUser.getText().toString().trim();
            String p = edtPass.getText().toString().trim();

            // fake login: accepts any non-empty credentials (easy to replace with backend)
            if(!u.isEmpty() && !p.isEmpty()) {
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Preencha usuário e senha", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
