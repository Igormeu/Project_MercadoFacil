package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_menu extends AppCompatActivity {

    private View btnCadastroPreco, btnListarPrecos, btnCadastroEst, btnSobreNos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCadastroPreco = findViewById(R.id.buttonmeunu2);
        btnCadastroEst = findViewById(R.id.buttonmeunu3);
        btnListarPrecos = findViewById(R.id.buttonmeunu1);
        btnSobreNos = findViewById(R.id.buttonmeunu4);

        btnCadastroPreco.setOnClickListener(v -> acessarCadastroPreco());
        btnCadastroEst.setOnClickListener(v -> acessarCadatroEst());
        btnListarPrecos.setOnClickListener(v -> acessarListarPrecos());
    }

    public void acessarCadastroPreco(){
        Intent intentCadastroPreco = new Intent(activity_menu.this, CadastrarPrecoActivity.class);
        startActivity(intentCadastroPreco);
    }

    public void acessarCadatroEst(){
        Intent intentCadastroEst = new Intent(activity_menu.this, CadastrarEstabelecimentoActivity.class);
        startActivity(intentCadastroEst);
    }

    public void acessarListarPrecos(){
        Intent intentListarPreco = new Intent(activity_menu.this, ExibirListaPrecosActivity.class);
        startActivity(intentListarPreco);
    }



}