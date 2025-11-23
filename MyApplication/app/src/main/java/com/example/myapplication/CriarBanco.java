package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriarBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "MercFacil.db";
    // Se precisar atualizar o banco, mude a versao para um valor maior que o atual
    private static final int VERSAO = 9;

    public CriarBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlEnderecos = "CREATE TABLE IF NOT EXISTS Enderecos (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nomeEstab TEXT, " +
                "rede TEXT, " +
                "rua TEXT, " +
                "cidade TEXT, " +
                "bairro TEXT, " +
                "estado TEXT, " +
                "cep TEXT" +
                ");";

        String sqlProdutos = "CREATE TABLE IF NOT EXISTS Produtos (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "quantidadePorUnidade DOUBLE, " +
                "unidadeMedida TEXT," +
                "marca TEXT" +
                ");";

        String sqlListaPrecos = "CREATE TABLE IF NOT EXISTS ListaPrecos (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idEndereco INTEGER, " +
                "idProduto INTEGER, " +
                "precoVenda REAL, " +
                "dataAtualizacao DATETIME, " +
                "FOREIGN KEY (idEndereco) REFERENCES Enderecos(_id), " +
                "FOREIGN KEY (idProduto) REFERENCES Produtos(_id)" +
                ");";

        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS Usuarios (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nomeCompleto TEXT, " +
                "email TEXT, " +
                "senha TEXT"  +
                ");";


        db.execSQL(sqlEnderecos);
        db.execSQL(sqlProdutos);
        db.execSQL(sqlListaPrecos);
        db.execSQL(sqlUsuarios);

        DadosIniciais.popularDados(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Enderecos");
        db.execSQL("DROP TABLE IF EXISTS Produtos");
        db.execSQL("DROP TABLE IF EXISTS ListaPrecos");
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        onCreate(db);
    }
}
