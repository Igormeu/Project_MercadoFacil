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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnRegistrar = findViewById(R.id.button8);
        btnLogin = findViewById(R.id.button4);
        edUsuerio = findViewById(R.id.editTextTextPassword3);
        edSenha = findViewById(R.id.editTextTextPassword8);
        bancoControle = new BancoControle(this);

        // 2. ADICIONAR O CLIQUE
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 3. INICIAR A TELA DE LOGIN
                Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(v -> realizarLogin());
    }

    // 🆕 Gerenciamento do ciclo de vida do banco (Recomendado)
    @Override
    protected void onResume() {
        super.onResume();
        // Abrir a conexão quando a Activity estiver ativa
        bancoControle.abrirBanco();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Fechar a conexão quando a Activity for pausada
        bancoControle.fecharBanco();
    }
    // Fim do gerenciamento do ciclo de vida

    public void realizarLogin(){
        String login = edUsuerio.getText().toString().trim();
        String senha = edSenha.getText().toString();

        // 1. Validação de campos vazios
        if (login.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha Login e Senha.", Toast.LENGTH_LONG).show();
            return;
        }

        // 2. Consulta ao banco de dados
        boolean credenciaisValidas = bancoControle.verificaLogin(login, senha);

        // 3. Resultado e Navegação
        if (credenciaisValidas) {
            Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();

            Intent intentLogin = new Intent(LoginActivity.this, ExibirListaPrecosActivity.class);
            startActivity(intentLogin);
            finish(); // Finaliza a tela de Login para que o usuário não volte com o botão 'Back'
        } else {
            Toast.makeText(this, "Credenciais inválidas. Tente novamente.", Toast.LENGTH_LONG).show();
            edSenha.setError("Login ou Senha incorretos");
        }
    }
}