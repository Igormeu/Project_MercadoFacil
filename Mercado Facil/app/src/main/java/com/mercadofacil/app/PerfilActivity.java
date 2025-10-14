package com.mercadofacil.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_perfil);

        Button btn = findViewById(R.id.btnLogout);
        btn.setOnClickListener(v -> {
            startActivity(new Intent(PerfilActivity.this, LoginActivity.class));
            finish();
        });
    }
}
