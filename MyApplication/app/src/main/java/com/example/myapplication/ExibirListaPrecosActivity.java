package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExibirListaPrecosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListaPrecoAdapter adapter;
    private BancoControle bancoControle;
    private EditText etPesquisa;
    private AutoCompleteTextView autoCompleteCidade;

    private List<ListaPrecoItem> listaCompletaPrecos = new ArrayList<>();

    private String nomeProdutoQuery = "";
    private String cidadeSelecionada = "Todas as Cidades";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibirlistaprecos);

        bancoControle = new BancoControle(this);
        etPesquisa = findViewById(R.id.et_pesquisa);
        autoCompleteCidade = findViewById(R.id.auto_complete_cidade);

        recyclerView = findViewById(R.id.rv_lista_precos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carregarDados();
        configurarDropdownCidades();
        configurarPesquisa();
    }


    @Override
    protected void onResume() {
        super.onResume();
        carregarDados();
    }


    private void carregarDados() {
        listaCompletaPrecos = new ArrayList<>();
        try {
            bancoControle.abrirBanco();

            listaCompletaPrecos = bancoControle.buscarTodosPrecos();

            if (listaCompletaPrecos.isEmpty()) {
                Toast.makeText(this, "Nenhum preço cadastrado.", Toast.LENGTH_LONG).show();
            }

            adapter = new ListaPrecoAdapter(listaCompletaPrecos);
            recyclerView.setAdapter(adapter);

            aplicarFiltro();

        } catch (Exception e) {
            Log.e("BuscaPrecoActivity", "Erro ao carregar dados iniciais.", e);
            Toast.makeText(this, "Erro ao carregar dados: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            bancoControle.fecharBanco();
        }
    }

    private void configurarDropdownCidades() {
        List<String> cidadesUnicas = new ArrayList<>();
        try {
            bancoControle.abrirBanco();
            cidadesUnicas = bancoControle.buscarCidadesUnicas();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao carregar cidades: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            bancoControle.fecharBanco();
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                cidadesUnicas
        );
        autoCompleteCidade.setAdapter(spinnerAdapter);

        if (!cidadesUnicas.isEmpty()) {
            autoCompleteCidade.setText(cidadesUnicas.get(0), false);
            cidadeSelecionada = cidadesUnicas.get(0);
        }

        autoCompleteCidade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cidadeSelecionada = parent.getItemAtPosition(position).toString();
                aplicarFiltro();
            }
        });
    }

    private void configurarPesquisa() {
        etPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não utilizado
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nomeProdutoQuery = s.toString();
                aplicarFiltro();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não utilizado
            }
        });
    }


    private void aplicarFiltro() {
        if (adapter != null) {
            adapter.filtrar(nomeProdutoQuery, cidadeSelecionada);
        }
    }
}