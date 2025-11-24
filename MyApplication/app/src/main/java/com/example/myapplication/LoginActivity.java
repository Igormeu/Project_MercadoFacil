package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;

    EditText edUsuerio, edSenha;

    private BancoControle bancoControle;
    private SessaoManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessaoManager(getApplicationContext());


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scroll_view_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnRegistrar = findViewById(R.id.button8);
        btnLogin = findViewById(R.id.button4);
        edUsuerio = findViewById(R.id.editTextTextPassword3);
        edSenha = findViewById(R.id.editTextTextPassword8);
        bancoControle = new BancoControle(this);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ao ir para a tela de registro, finalize esta Activity
                Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(v -> realizarLogin());

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

    public void realizarLogin(){
        String login = edUsuerio.getText().toString().trim();
        String senha = edSenha.getText().toString();

        if (login.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha Login e Senha.", Toast.LENGTH_LONG).show();
            return;
        }

        boolean credenciaisValidas = bancoControle.verificaLogin(login, senha);

        if (credenciaisValidas) {

            String nomeCompleto = bancoControle.getNomeCompletoPorEmail(login);
            if (nomeCompleto != null) {
                session.criarSessaoLogin(login, nomeCompleto);
            } else {

                session.criarSessaoLogin(login, "Usuário");
            }

            Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();

            Intent intentLogin = new Intent(LoginActivity.this, activity_menu.class);

            intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentLogin);
            finish();
        } else {
            Toast.makeText(this, "Credenciais inválidas. Tente novamente.", Toast.LENGTH_LONG).show();
            edSenha.setError("Login ou Senha incorretos");
        }
    }
}