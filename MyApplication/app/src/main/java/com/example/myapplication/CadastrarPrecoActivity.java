package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CadastrarPrecoActivity extends AppCompatActivity {

    private BancoControle bancoControle;
    private Spinner spinnerEnderecos;
    private Spinner spinnerProdutos;
    private EditText editPrecoVenda;
    private Button btnSalvarPreco;

    // Mapas para armazenar a relação Nome -> ID
    private Map<String, Integer> mapaEnderecos;
    private Map<String, Integer> mapaProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_lista);

        bancoControle = new BancoControle(this);
        spinnerEnderecos = findViewById(R.id.spinner_enderecos);
        spinnerProdutos = findViewById(R.id.spinner_produtos);
        editPrecoVenda = findViewById(R.id.edit_preco_venda);
        btnSalvarPreco = findViewById(R.id.btn_salvar_preco);

        try {
            bancoControle.abrirBanco();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao abrir o banco de dados.", Toast.LENGTH_LONG).show();
            finish(); // Encerra a activity se não conseguir abrir o banco
            return;
        }

        // 1. Carregar e popular Spinners
        carregarEnderecos();
        carregarProdutos();

        // 2. Configurar botão de salvar
        btnSalvarPreco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarListaPreco();
            }
        });
    }

    // --- MÉTODOS DE CARREGAMENTO DE DADOS ---

    private void carregarEnderecos() {
        Cursor cursor = bancoControle.carregaDados("Enderecos");
        List<String> nomesEnderecos = new ArrayList<>();
        mapaEnderecos = new HashMap<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Captura o ID e o nome. Os índices são 0 para _id e 1 para nomeEstab (ver BancoControle)
                int id = cursor.getInt(0);
                String nome = cursor.getString(1); // coluna nomeEstab

                nomesEnderecos.add(nome);
                mapaEnderecos.put(nome, id);

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            nomesEnderecos.add("Nenhum Endereço Cadastrado");
            Toast.makeText(this, "⚠️ Cadastre um Endereço primeiro!", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, nomesEnderecos);
        spinnerEnderecos.setAdapter(adapter);
    }

    private void carregarProdutos() {
        Cursor cursor = bancoControle.carregaDados("Produtos");
        List<String> nomesProdutos = new ArrayList<>();
        mapaProdutos = new HashMap<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Captura o ID e o nome. Os índices são 0 para _id e 1 para nome
                int id = cursor.getInt(0);
                String nome = cursor.getString(1); // coluna nome

                nomesProdutos.add(nome);
                mapaProdutos.put(nome, id);

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            nomesProdutos.add("Nenhum Produto Cadastrado");
            Toast.makeText(this, "⚠️ Cadastre um Produto primeiro!", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, nomesProdutos);
        spinnerProdutos.setAdapter(adapter);
    }

    // --- MÉTODO DE SALVAR ---

    private void salvarListaPreco() {
        // 1. Validar seleção e preço
        String nomeEstabSelecionado = (String) spinnerEnderecos.getSelectedItem();
        String nomeProdutoSelecionado = (String) spinnerProdutos.getSelectedItem();
        String precoString = editPrecoVenda.getText().toString();

        if (nomeEstabSelecionado.equals("Nenhum Endereço Cadastrado") ||
                nomeProdutoSelecionado.equals("Nenhum Produto Cadastrado")) {
            Toast.makeText(this, "É necessário ter Endereços e Produtos cadastrados.", Toast.LENGTH_LONG).show();
            return;
        }

        if (precoString.isEmpty()) {
            Toast.makeText(this, "Insira o preço de venda.", Toast.LENGTH_SHORT).show();
            return;
        }

        double preco;
        try {
            preco = Double.parseDouble(precoString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Preço inválido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Obter IDs
        int idEndereco = mapaEnderecos.get(nomeEstabSelecionado);
        int idProduto = mapaProdutos.get(nomeProdutoSelecionado);

        // 3. Obter data atual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dataAtualizacao = sdf.format(new Date());

        // 4. Inserir no banco
        String resultado = bancoControle.insereListaPreco(idEndereco, idProduto, preco, dataAtualizacao);

        Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

        if (resultado.startsWith("✅")) {
            // Limpar campo após sucesso
            editPrecoVenda.setText("");
        }
    }

    // --- MÉTODOS DO CICLO DE VIDA ---
    @Override
    protected void onResume() {
        super.onResume();
        // Reabre o banco, caso tenha sido fechado
        try {
            bancoControle.abrirBanco();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao reabrir o banco de dados.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Fecha o banco quando a Activity não está visível
        bancoControle.fecharBanco();
    }
}