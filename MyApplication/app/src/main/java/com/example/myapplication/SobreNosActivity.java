package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SobreNosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sobre_nos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_sobre_nos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvVersao = findViewById(R.id.tv_versao);
        try {
            String versaoApp = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersao.setText("Versão: " + versaoApp);
        } catch (Exception e) {
            tvVersao.setText("Versão: 1.0");
        }
    }
}