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

    // 🆕 Gerenciamento do ciclo de vida do banco (Recomendado)
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

        // 🆕 4. VALIDAÇÃO DE USUÁRIO EXISTENTE
        if (bancoControle.verificaUsuarioExiste(loginEmail)) {
            Toast.makeText(this, "❌ Erro: Este e-mail já está cadastrado!", Toast.LENGTH_LONG).show();
            edEmail.setError("E-mail já registrado");
            return;
        }

        String resultado = bancoControle.insereUsuario(loginEmail, senha, nome);

        Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

        if (resultado.startsWith("✅")) {
            // Se a inserção foi bem-sucedida, volta para a tela de login
            voltarLogin();
        }


    }

 /*   public void verTabelaUsuarios() {
        Cursor cursor = bancoControle.carregaDados("Usuarios");

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Nenhum usuário cadastrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Iterar sobre o Cursor e exibir no Logcat
        Log.d("DB_USUARIOS", "--- INÍCIO DA TABELA USUARIOS ---");

        // Obter os índices das colunas (ATUALIZADO para nomeCompleto e email)
        int idIndex = cursor.getColumnIndex("_id");
        int nomeCompletoIndex = cursor.getColumnIndex("nomeCompleto"); // 🆕 Novo campo
        int emailIndex = cursor.getColumnIndex("email");             // 🔁 Substitui 'login'
        int senhaIndex = cursor.getColumnIndex("senha");

        // Reiniciar para a primeira linha para garantir
        cursor.moveToFirst();

        do {
            // Obter os valores para a linha atual
            String id = (idIndex != -1) ? cursor.getString(idIndex) : "N/A";
            String nomeCompleto = (nomeCompletoIndex != -1) ? cursor.getString(nomeCompletoIndex) : "N/A";
            String email = (emailIndex != -1) ? cursor.getString(emailIndex) : "N/A";
            String senha = (senhaIndex != -1) ? cursor.getString(senhaIndex) : "N/A";

            // Imprimir no Logcat com os novos campos
            Log.d("DB_USUARIOS",
                    "ID: " + id +
                            ", Nome Completo: " + nomeCompleto +
                            ", Email: " + email +
                            ", Senha: " + senha);

        } while (cursor.moveToNext()); // Mover para a próxima linha

        Log.d("DB_USUARIOS", "--- FIM DA TABELA USUARIOS ---");

        // 3. Fechar o Cursor (MUITO IMPORTANTE!)
        cursor.close();
    }
*/

}