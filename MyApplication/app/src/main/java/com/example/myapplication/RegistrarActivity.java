package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.database.Cursor;
import android.util.Log;

public class RegistrarActivity extends AppCompatActivity {

    TextView btnVoltar;
    Button btnRegistrar;

    EditText edNome, edSenha, edConfirmar, edEmail;
    private BancoControle bancoControle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bancoControle = new BancoControle(this);

        btnVoltar = findViewById(R.id.textViewHasAccount);
        btnRegistrar = findViewById(R.id.buttonRegister);
        edNome = findViewById(R.id.editTextName);
        edEmail = findViewById(R.id.editTextEmail);
        edSenha = findViewById(R.id.editTextPassword);
        edConfirmar = findViewById(R.id.editTextConfirmPassword);

        btnVoltar.setOnClickListener(v -> voltarLogin());
        btnRegistrar.setOnClickListener(v -> registrarLogin());
    }

    @Override
    protected void onResume() {
        super.onResume();
        bancoControle.abrirBanco();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bancoControle.fecharBanco();
    }

    public void voltarLogin(){
        Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void registrarLogin(){
        String nome = edNome.getText().toString().trim();
        String loginEmail = edEmail.getText().toString().trim();
        String senha = edSenha.getText().toString();
        String confirmarSenha = edConfirmar.getText().toString();

        if (nome.isEmpty() || loginEmail.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_LONG).show();
            edSenha.setError("Senhas diferentes");
            edConfirmar.setError("Senhas diferentes");
            return;
        }

        if (bancoControle.verificaUsuarioExiste(loginEmail)) {
            Toast.makeText(this, "❌ Erro: Este e-mail já está cadastrado!", Toast.LENGTH_LONG).show();
            edEmail.setError("E-mail já registrado");
            return;
        }

        String resultado = bancoControle.insereUsuario(loginEmail, senha, nome);

        Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

        if (resultado.startsWith("✅")) {
            voltarLogin();
        }


    }

}