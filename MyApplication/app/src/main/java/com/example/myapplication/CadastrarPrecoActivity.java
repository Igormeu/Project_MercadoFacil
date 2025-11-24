package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

    private AutoCompleteTextView autoCompleteNomesEstab;
    private AutoCompleteTextView autoCompleteDetalheEnd;
    private AutoCompleteTextView autoCompleteMarcas;
    private AutoCompleteTextView autoCompleteProdutos;

    private EditText editPrecoVenda;
    private Button btnSalvarPreco;
    private Map<String, Map<String, Integer>> mapaDetalhesEnderecosPorNome;
    private Map<String, List<ProdutoDetalhe>> mapaProdutosPorNome;
    private Map<String, Integer> mapaProdutos;
    private static class ProdutoDetalhe {
        int id;
        String marca;

        public ProdutoDetalhe(int id, String marca) {
            this.id = id;
            this.marca = marca;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_lista);

        bancoControle = new BancoControle(this);

        autoCompleteNomesEstab = findViewById(R.id.auto_complete_enderecos);
        autoCompleteDetalheEnd = findViewById(R.id.auto_complete_detalhe_endereco);
        autoCompleteMarcas = findViewById(R.id.auto_complete_marcas);
        autoCompleteProdutos = findViewById(R.id.auto_complete_produtos);

        editPrecoVenda = findViewById(R.id.edit_preco_venda);
        btnSalvarPreco = findViewById(R.id.btn_salvar_preco);

        try {
            bancoControle.abrirBanco();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao abrir o banco de dados.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        carregarNomesEnderecos();
        carregarProdutos();

        autoCompleteNomesEstab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nomeEstabSelecionado = (String) parent.getItemAtPosition(position);
                carregarDetalhesEndereco(nomeEstabSelecionado);
            }
        });

        autoCompleteProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String produtoSelecionado = (String) parent.getItemAtPosition(position);
                carregarMarcasDisponiveis(produtoSelecionado);
            }
        });

        autoCompleteMarcas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        btnSalvarPreco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarListaPreco();
            }
        });
    }

    private void carregarNomesEnderecos() {
        Cursor cursor = bancoControle.carregaDados("Enderecos");
        List<String> nomesEnderecos = new ArrayList<>();
        mapaDetalhesEnderecosPorNome = new HashMap<>();

        if (cursor != null && cursor.moveToFirst()) {
            Map<String, Boolean> nomesUnicos = new HashMap<>();

            do {
                String nome = cursor.getString(1);

                if (!nomesUnicos.containsKey(nome)) {
                    nomesUnicos.put(nome, true);
                    nomesEnderecos.add(nome);

                    Map<String, Integer> detalhes = bancoControle.buscarEnderecosPorNome(nome);
                    mapaDetalhesEnderecosPorNome.put(nome, detalhes);
                }

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            nomesEnderecos.add("Nenhum Estabelecimento Cadastrado");
            Toast.makeText(this, "⚠️ Cadastre um Endereço primeiro!", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, nomesEnderecos);
        autoCompleteNomesEstab.setAdapter(adapter);

        if (!nomesEnderecos.isEmpty()) {
            autoCompleteNomesEstab.setText(nomesEnderecos.get(0), false);
            carregarDetalhesEndereco(nomesEnderecos.get(0));
        } else {
            carregarDetalhesEndereco(null);
        }
    }

    private void carregarDetalhesEndereco(String nomeEstab) {
        List<String> detalhes = new ArrayList<>();
        String detalheInicial = "Selecione um Endereço";

        if (nomeEstab != null && mapaDetalhesEnderecosPorNome.containsKey(nomeEstab)) {
            Map<String, Integer> mapaAtual = mapaDetalhesEnderecosPorNome.get(nomeEstab);
            detalhes.addAll(mapaAtual.keySet());
            if (!detalhes.isEmpty()) {
                detalheInicial = detalhes.get(0);
            }
        } else {
            detalhes.add(detalheInicial);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, detalhes);
        autoCompleteDetalheEnd.setAdapter(adapter);

        autoCompleteDetalheEnd.setText(detalheInicial, false);
    }

    private void carregarProdutos() {
        Cursor cursor = bancoControle.carregaDados("Produtos");
        List<String> nomesProdutos = new ArrayList<>();
        mapaProdutosPorNome = new HashMap<>();
        mapaProdutos = new HashMap<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nome = cursor.getString(1);
                String marca = cursor.getString(4);

                if (!mapaProdutosPorNome.containsKey(nome)) {
                    mapaProdutosPorNome.put(nome, new ArrayList<>());
                    nomesProdutos.add(nome);
                }

                mapaProdutosPorNome.get(nome).add(new ProdutoDetalhe(id, marca));

                String chaveFinal = nome + " (" + marca + ")";
                mapaProdutos.put(chaveFinal, id);

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            nomesProdutos.add("Nenhum Produto Cadastrado");
            Toast.makeText(this, "⚠️ Cadastre um Produto primeiro!", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, nomesProdutos);
        autoCompleteProdutos.setAdapter(adapter);

        if (!nomesProdutos.isEmpty()) {
            autoCompleteProdutos.setText(nomesProdutos.get(0), false);
            carregarMarcasDisponiveis(nomesProdutos.get(0));
        } else {
            carregarMarcasDisponiveis(null);
        }
    }


    private void carregarMarcasDisponiveis(String nomeProduto) {
        List<String> marcas = new ArrayList<>();
        String marcaInicial = "Selecione a Marca";

        autoCompleteMarcas.setText(marcaInicial, false);

        if (nomeProduto != null && mapaProdutosPorNome.containsKey(nomeProduto)) {
            List<ProdutoDetalhe> detalhes = mapaProdutosPorNome.get(nomeProduto);
            for (ProdutoDetalhe detalhe : detalhes) {
                marcas.add(detalhe.marca);
            }

            if (!marcas.isEmpty()) {
                marcaInicial = marcas.get(0);
            }
        } else {
            marcas.add(marcaInicial);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, marcas);
        autoCompleteMarcas.setAdapter(adapter);
        autoCompleteMarcas.setText(marcaInicial, false);
    }


    private void salvarListaPreco() {
        String nomeEstabSelecionado = autoCompleteNomesEstab.getText().toString();
        String detalheEndSelecionado = autoCompleteDetalheEnd.getText().toString();
        String nomeProdutoSelecionado = autoCompleteProdutos.getText().toString();
        String marcaSelecionada = autoCompleteMarcas.getText().toString(); // **NOVO**
        String precoString = editPrecoVenda.getText().toString();

        if (nomeEstabSelecionado.equals("Nenhum Estabelecimento Cadastrado") ||
                detalheEndSelecionado.equals("Selecione um Endereço") ||
                nomeProdutoSelecionado.equals("Nenhum Produto Cadastrado")) {

            Toast.makeText(this, "É necessário selecionar um Estabelecimento e um Produto válidos.", Toast.LENGTH_LONG).show();
            return;
        }

        if (marcaSelecionada.equals("Selecione a Marca") || marcaSelecionada.isEmpty()) {
            Toast.makeText(this, "É necessário selecionar a Marca do Produto.", Toast.LENGTH_LONG).show();
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

        Map<String, Integer> detalhesAtuais = mapaDetalhesEnderecosPorNome.get(nomeEstabSelecionado);

        if (detalhesAtuais == null || !detalhesAtuais.containsKey(detalheEndSelecionado)) {
            Toast.makeText(this, "Erro: Detalhe do endereço não encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        int idEndereco = detalhesAtuais.get(detalheEndSelecionado);

        String chaveFinalProduto = nomeProdutoSelecionado + " (" + marcaSelecionada + ")";

        if (!mapaProdutos.containsKey(chaveFinalProduto)) {
            Toast.makeText(this, "Erro: Combinação de Produto e Marca não encontrada.", Toast.LENGTH_SHORT).show();
            return;
        }

        int idProduto = mapaProdutos.get(chaveFinalProduto);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dataAtualizacao = sdf.format(new Date());

        String resultado = bancoControle.insereListaPreco(idEndereco, idProduto, preco, dataAtualizacao);

        Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

        if (resultado.startsWith("✅")) {
            editPrecoVenda.setText("");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            bancoControle.abrirBanco();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao reabrir o banco de dados.", Toast.LENGTH_SHORT).show();
        }
        carregarNomesEnderecos();
        carregarProdutos();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bancoControle.fecharBanco();
    }
}