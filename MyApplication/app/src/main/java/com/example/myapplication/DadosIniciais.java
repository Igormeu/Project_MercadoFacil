package com.example.myapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DadosIniciais {

    private static final String UNIDADE_KG = "KG";
    private static final String UNIDADE_LITRO = "L";
    private static final String UNIDADE_G = "G";
    private static final String TABELA_ENDERECOS = "Enderecos";
    private static final String TABELA_PRODUTOS = "Produtos";
    private static final String TABELA_PRECOS = "ListaPrecos";

    /**
     * Popula as tabelas Produtos, Enderecos e ListaPrecos com dados de teste,
     * utilizando transações para garantir a velocidade e a atomicidade.
     * @param db O objeto SQLiteDatabase.
     */
    public static void popularDados(SQLiteDatabase db) {

        if (db == null) {
            Log.e("DadosIniciais", "SQLiteDatabase está NULL.");
            return;
        }

        Log.d("DadosIniciais", "Iniciando inserção de dados por transação...");
        db.beginTransaction();

        try {

            String dataAntiga = "2025-11-21 10:00:00";
            String dataRecente = "2025-11-22 14:00:00";
            String dataNova = "2025-11-23 01:00:00";

            insereProduto(db, "Arroz Branco", 5.0, UNIDADE_KG, "Camil"); // ID 1
            insereProduto(db, "Feijão Preto", 1.0, UNIDADE_KG, "Kicaldo"); // ID 2
            insereProduto(db, "Leite Integral", 1.0, UNIDADE_LITRO, "Piracanjuba"); // ID 3
            insereProduto(db, "Sabão em Pó", 1.6, UNIDADE_KG, "Omo"); // ID 4
            insereProduto(db, "Pão de Forma", 400.0, UNIDADE_G, "Pullman"); // ID 5
            insereProduto(db, "Café Torrado", 500.0, UNIDADE_G, "3 Corações"); // ID 6
            insereProduto(db, "Arroz Branco", 5.0, UNIDADE_KG, "Urbano"); // ID 7
            insereProduto(db, "Feijão Preto", 1.0, UNIDADE_KG, "Broto Legal"); // ID 8
            insereProduto(db, "Leite Integral", 1.0, UNIDADE_LITRO, "Nestlé"); // ID 9
            insereProduto(db, "Sabão em Pó", 1.0, UNIDADE_KG, "Tixan"); // ID 10

            insereEndereco(db, "Assaí Atacadista", "Assaí", "Av. da Matriz, 123", "Fortaleza", "Centro", "CE", "60000-000"); // ID 1
            insereEndereco(db, "Pão de Açúcar", "Pão de Açúcar", "Rua das Flores, 456", "Fortaleza", "Aldeota", "CE", "60000-001"); // ID 2
            insereEndereco(db, "Mercadinho Central", "", "Rua Principal, 789", "Caucaia", "Vila Nova", "CE", "61000-000"); // ID 3
            insereEndereco(db, "Superbox Atacado", "Superbox", "Rodovia Norte, 100", "Caucaia", "Jardim", "CE", "61000-001"); // ID 4
            insereEndereco(db, "Carrefour Sul", "Carrefour", "Av. Sul, 900", "Fortaleza", "Messejana", "CE", "60000-002"); // ID 5


            inserePreco(db, 2, 7, 29.90, dataRecente);

            inserePreco(db, 4, 7, 28.50, dataNova);

            inserePreco(db, 1, 9, 5.15, dataRecente);

            inserePreco(db, 1, 1, 28.50, dataAntiga);
            inserePreco(db, 5, 1, 27.90, dataAntiga);
            inserePreco(db, 4, 1, 27.00, dataAntiga);
            inserePreco(db, 1, 3, 4.99, dataAntiga);
            inserePreco(db, 2, 3, 5.50, dataAntiga);
            inserePreco(db, 3, 9, 4.80, dataAntiga);
            inserePreco(db, 1, 4, 21.99, dataAntiga);
            inserePreco(db, 5, 4, 20.99, dataAntiga);
            inserePreco(db, 4, 10, 18.50, dataAntiga);
            inserePreco(db, 3, 10, 19.00, dataAntiga);
            inserePreco(db, 3, 2, 7.80, dataAntiga);
            inserePreco(db, 5, 8, 8.20, dataAntiga);
            inserePreco(db, 2, 5, 8.99, dataAntiga);
            inserePreco(db, 4, 6, 15.99, dataAntiga);
            inserePreco(db, 5, 6, 14.99, dataAntiga);

            insereUsuario(db, "teste@email.com", "123456", "Usuário de Teste");

            db.setTransactionSuccessful();
            Log.d("DadosIniciais", "População de dados concluída com sucesso.");

        } catch (Exception e) {
            Log.e("DadosIniciais", "Falha na transação de inserção de dados.", e);
        } finally {
            db.endTransaction();
        }
    }


    private static void insereProduto(SQLiteDatabase db, String nome, double quantidadePorUnidade, String unidadeMedida, String marca) {
        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        valores.put("quantidadePorUnidade", quantidadePorUnidade);
        valores.put("unidadeMedida", unidadeMedida);
        valores.put("marca", marca);
        db.insert(TABELA_PRODUTOS, null, valores);
    }

    private static void insereEndereco(SQLiteDatabase db, String nomeEstab, String rede, String rua, String cidade, String bairro, String estado, String cep) {
        ContentValues valores = new ContentValues();
        valores.put("nomeEstab", nomeEstab);
        valores.put("rede", rede);
        valores.put("rua", rua);
        valores.put("cidade", cidade);
        valores.put("bairro", bairro);
        valores.put("estado", estado);
        valores.put("cep", cep);
        db.insert(TABELA_ENDERECOS, null, valores);
    }

    private static void inserePreco(SQLiteDatabase db, int idEndereco, int idProduto, double precoVenda, String dataAtualizacao) {
        ContentValues valores = new ContentValues();
        valores.put("idEndereco", idEndereco);
        valores.put("idProduto", idProduto);
        valores.put("precoVenda", precoVenda);
        valores.put("dataAtualizacao", dataAtualizacao);
        db.insert(TABELA_PRECOS, null, valores);
    }

    private static void insereUsuario(SQLiteDatabase db, String email, String senha, String nomeCompleto) {
        ContentValues valores = new ContentValues();
        valores.put("nomeCompleto", nomeCompleto);
        valores.put("email", email);
        valores.put("senha", senha);
        db.insert("Usuarios", null, valores);
    }
}