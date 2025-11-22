package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CadastrarEstabelecimentoActivity extends AppCompatActivity {

    private EditText etNomeEstab, etRede, etRua, etCidade, etBairro, etEstado, etCep;
    private BancoControle bancoControle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_estabelecimento);

        bancoControle = new BancoControle(this);

        etNomeEstab = findViewById(R.id.et_nome_estab);
        etRede = findViewById(R.id.et_rede);
        etRua = findViewById(R.id.et_rua);
        etCidade = findViewById(R.id.et_cidade);
        etBairro = findViewById(R.id.et_bairro);
        etEstado = findViewById(R.id.et_estado);
        etCep = findViewById(R.id.et_cep);
        Button btnCadastrar = findViewById(R.id.btn_cadastrar_estabelecimento);

        btnCadastrar.setOnClickListener(v -> cadastrarEstabelecimento());
    }

    private void cadastrarEstabelecimento() {
        // 1. Coleta os dados
        String nomeEstab = etNomeEstab.getText().toString().trim();
        String rede = etRede.getText().toString().trim();
        String rua = etRua.getText().toString().trim();
        String cidade = etCidade.getText().toString().trim();
        String bairro = etBairro.getText().toString().trim();
        String estado = etEstado.getText().toString().trim();
        String cep = etCep.getText().toString().trim();

        if (nomeEstab.isEmpty() || rua.isEmpty() || cidade.isEmpty() || estado.isEmpty() || cep.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha Nome, Rua, Cidade, Estado e CEP.", Toast.LENGTH_LONG).show();
            return;
        }

        String resultado = "";
        try {

            bancoControle.abrirBanco();

            resultado = bancoControle.insereEndereco(
                    nomeEstab,
                    rede,
                    rua,
                    cidade,
                    bairro,
                    estado,
                    cep
            );

            // 4. Feedback para o usuário
            Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

            if (resultado.startsWith("✅")) {
                // Limpa os campos após o sucesso
                limparCampos();
            }

        } catch (Exception e) {
            Log.e("CadastroEstabelecimento", "Erro ao acessar o banco de dados.", e);
            Toast.makeText(this, "Erro crítico ao cadastrar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            bancoControle.fecharBanco();
        }
    }

    private void limparCampos() {
        etNomeEstab.setText("");
        etRede.setText("");
        etRua.setText("");
        etCidade.setText("");
        etBairro.setText("");
        etEstado.setText("");
        etCep.setText("");
        etNomeEstab.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            bancoControle.abrirBanco();
        } catch (Exception e) {
            // Log de erro
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        bancoControle.fecharBanco();
    }
}