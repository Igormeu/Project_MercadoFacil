package com.mercadofacil.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_lista);

        rv = findViewById(R.id.rvProdutos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> itens = new ArrayList<>();
        for(int i=1;i<=20;i++) itens.add("Produto " + i);

        ProdutoAdapter adapter = new ProdutoAdapter(itens);
        rv.setAdapter(adapter);
    }
}
