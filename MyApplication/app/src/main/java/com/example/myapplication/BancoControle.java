package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BancoControle {

    SQLiteDatabase db;
    private CriarBanco banco;

    public BancoControle(Context context){
        banco = new CriarBanco(context);
    }

    // --- MÉTODOS DE CONTROLE DO BANCO ---
    public void abrirBanco() throws SQLiteException {
        db = banco.getWritableDatabase();
    }

    public void fecharBanco() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // --- MÉTODOS DE INSERÇÃO ---
    public String insereEndereco(String nomeEstab, String rede, String rua,
                                 String cidade, String bairro, String estado, String cep) {
        ContentValues valores = new ContentValues();
        long resultado = -1;

        try {
            valores.put("nomeEstab", nomeEstab);
            valores.put("rede", rede);
            valores.put("rua", rua);
            valores.put("cidade", cidade);
            valores.put("bairro", bairro);
            valores.put("estado", estado);
            valores.put("cep", cep);

            resultado = db.insert("Enderecos", null, valores);

        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao inserir endereço", e);
        }

        if (resultado == -1)
            return "❌ Erro ao inserir endereço";
        else
            return "✅ Endereço inserido com sucesso";
    }

    public String insereProduto(String nome, double quantidadePorUnidade, String unidadeMedida, String marca) {
        ContentValues valores = new ContentValues();
        long resultado = -1;

        try {
            valores.put("nome", nome);
            valores.put("quantidadePorUnidade", quantidadePorUnidade);
            valores.put("unidadeMedida", unidadeMedida);
            valores.put("marca", marca);

            resultado = db.insert("Produtos", null, valores);

        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao inserir produto", e);
        }

        if (resultado == -1)
            return "❌ Erro ao inserir produto";
        else
            return "✅ Produto inserido com sucesso";
    }


    public String insereListaPreco(int idEndereco, int idProduto, double precoVenda, String dataAtualizacao) {
        ContentValues valores = new ContentValues();
        long resultado = -1;

        try {
            valores.put("idEndereco", idEndereco);
            valores.put("idProduto", idProduto);
            valores.put("precoVenda", precoVenda);
            valores.put("dataAtualizacao", dataAtualizacao);

            resultado = db.insert("ListaPrecos", null, valores);

        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao inserir lista de preço", e);
        }

        if (resultado == -1)
            return "❌ Erro ao inserir lista de preço";
        else
            return "✅ Lista de preço inserida com sucesso";
    }

    public String insereUsuario(String email, String senha, String nomeCompleto) {
        ContentValues valores = new ContentValues();
        long resultado = -1;

        try {
            valores.put("nomeCompleto", nomeCompleto);
            valores.put("email", email);
            valores.put("senha", senha);

            resultado = db.insert("Usuarios", null, valores);

        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao inserir usuário", e);
        }

        if (resultado == -1)
            return "❌ Erro ao inserir usuário";
        else
            return "✅ Usuário inserido com sucesso";
    }

    // --- MÉTODO DE CONSULTA ---

    public Cursor carregaDados(String tabelaSelecionada) {
        Cursor cursor;
        String[] campos;

        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        switch (tabelaSelecionada) {
            case "Enderecos":
                campos = new String[] {"_id", "nomeEstab", "rede", "rua", "cidade", "bairro", "estado", "cep"};
                break;
            case "Produtos":
                campos = new String[] {"_id", "nome", "quantidadePorUnidade", "unidadeMedida", "marca"};
                break;
            case "ListaPrecos":
                campos = new String[] {"_id", "idEndereco", "idProduto", "precoVenda", "dataAtualizacao"};
                break;
            case "Usuarios":
                campos = new String[] {"_id","nomeCompleto", "email", "senha"};
                break;
            default:
                throw new IllegalArgumentException("Tabela inválida: " + tabelaSelecionada);
        }

        cursor = db.query(tabelaSelecionada, campos, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public boolean verificaUsuarioExiste(String login) {
        Cursor cursor = null;
        boolean existe = false;

        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        try {
            String[] campos = new String[]{"email"};
            String where = "email = ?";
            String[] argumentos = new String[]{login};

            cursor = db.query("Usuarios", campos, where, argumentos, null, null, null, "1");

            if (cursor != null && cursor.getCount() > 0) {
                existe = true;
            }

        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao verificar existência do usuário", e);
            existe = false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return existe;
    }


    public boolean verificaLogin(String login, String senha) {
        Cursor cursor = null;
        boolean loginValido = false;

        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        try {
            String[] campos = new String[]{"_id"};
            String where = "email = ? AND senha = ?";
            String[] argumentos = new String[]{login, senha};

            cursor = db.query("Usuarios", campos, where, argumentos, null, null, null, "1");

            if (cursor != null && cursor.getCount() > 0) {
                loginValido = true;
            }

        } catch (Exception e) {
            Log.e("BancoControle", "Erro na verificação de login", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return loginValido;
    }

    public List<ListaPrecoItem> buscarTodosPrecos() {
        List<ListaPrecoItem> listaPrecos = new ArrayList<>();
        Cursor cursor = null;

        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        String query = "SELECT " +
                "P.nome AS nomeProduto, " +
                "P.marca AS marcaProduto, " +
                "P.quantidadePorUnidade AS quantidade, " +
                "P.unidadeMedida AS unidade, " +
                "E.nomeEstab AS nomeEstabelecimento, " +
                "E.rua AS ruaEstabelecimento, " +
                "E.cidade AS cidadeEstabelecimento, " +
                "LP.dataAtualizacao, " +
                "LP.precoVenda " +
                "FROM ListaPrecos LP " +
                "INNER JOIN Produtos P ON LP.idProduto = P._id " +
                "INNER JOIN Enderecos E ON LP.idEndereco = E._id " +
                "ORDER BY LP.dataAtualizacao DESC";

        try {
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String nomeProduto = cursor.getString(cursor.getColumnIndexOrThrow("nomeProduto"));
                    String marcaProduto = cursor.getString(cursor.getColumnIndexOrThrow("marcaProduto"));

                    double quantidade = cursor.getDouble(cursor.getColumnIndexOrThrow("quantidade"));
                    String unidade = cursor.getString(cursor.getColumnIndexOrThrow("unidade"));

                    String nomeEstabelecimento = cursor.getString(cursor.getColumnIndexOrThrow("nomeEstabelecimento"));
                    String ruaEstabelecimento = cursor.getString(cursor.getColumnIndexOrThrow("ruaEstabelecimento"));
                    String cidade = cursor.getString(cursor.getColumnIndexOrThrow("cidadeEstabelecimento"));
                    String dataAtualizacao = cursor.getString(cursor.getColumnIndexOrThrow("dataAtualizacao"));
                    double precoVenda = cursor.getDouble(cursor.getColumnIndexOrThrow("precoVenda"));

                    String localidade = nomeEstabelecimento + ", " + ruaEstabelecimento;

                    ListaPrecoItem item = new ListaPrecoItem(
                            nomeProduto,
                            localidade,
                            dataAtualizacao,
                            precoVenda,
                            cidade,
                            marcaProduto,
                            quantidade,
                            unidade
                    );
                    listaPrecos.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao buscar todos os preços.", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listaPrecos;
    }

    public Map<String, Integer> buscarEnderecosPorNome(String nomeEstab) {
        Map<String, Integer> detalhesEnderecos = new HashMap<>();
        Cursor cursor = null;

        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        String[] campos = new String[]{"_id", "rua", "cidade"};
        String where = "nomeEstab = ?";
        String[] argumentos = new String[]{nomeEstab};

        try {
            cursor = db.query("Enderecos", campos, where, argumentos, null, null, "rua ASC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    String rua = cursor.getString(cursor.getColumnIndexOrThrow("rua"));
                    String cidade = cursor.getString(cursor.getColumnIndexOrThrow("cidade"));

                    String detalhe = rua + ", " + cidade;

                    detalhesEnderecos.put(detalhe, id);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao buscar endereços por nome: " + nomeEstab, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return detalhesEnderecos;
    }

    public List<String> buscarCidadesUnicas() {
        List<String> cidades = new ArrayList<>();
        cidades.add("Todas as Cidades");

        Cursor cursor = null;
        String query = "SELECT DISTINCT cidade FROM Enderecos ORDER BY cidade ASC";

        try {
            if (db == null || !db.isOpen()) {
                db = banco.getReadableDatabase();
            }

            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String cidade = cursor.getString(cursor.getColumnIndexOrThrow("cidade"));
                    cidades.add(cidade);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao buscar cidades únicas.", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            fecharBanco();
        }
        return cidades;
    }

    public List<String> buscarMarcasUnicas() {
        List<String> marcas = new ArrayList<>();
        Cursor cursor = null;

        String query = "SELECT DISTINCT marca FROM Produtos ORDER BY marca ASC";

        try {
            if (db == null || !db.isOpen()) {
                db = banco.getReadableDatabase();
            }

            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String marca = cursor.getString(cursor.getColumnIndexOrThrow("marca"));
                    marcas.add(marca);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao buscar marcas únicas.", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return marcas;
    }

    public String buscarMarcaPorNomeProduto(String nomeProduto) {
        Cursor cursor = null;
        String marca = null;

        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        String[] campos = new String[]{"marca"};
        String where = "nome = ?";
        String[] argumentos = new String[]{nomeProduto};

        try {
            cursor = db.query("Produtos", campos, where, argumentos, null, null, null, "1");

            if (cursor != null && cursor.moveToFirst()) {
                marca = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao buscar marca por nome do produto: " + nomeProduto, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return marca;
    }

    public List<String> buscarUltimasAtualizacoesPreco() {
        List<String> ultimasAtualizacoes = new ArrayList<>();
        Cursor cursor = null;

        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        String query = "SELECT " +
                "P.nome AS nomeProduto, " +
                "E.nomeEstab AS nomeEstabelecimento, " +
                "E.cidade AS cidadeEstabelecimento, " +
                "LP.precoVenda, " +
                "LP.dataAtualizacao " +
                "FROM ListaPrecos LP " +
                "INNER JOIN Produtos P ON LP.idProduto = P._id " +
                "INNER JOIN Enderecos E ON LP.idEndereco = E._id " +
                "ORDER BY LP.dataAtualizacao DESC " +
                "LIMIT 2";

        try {
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

                do {
                    String nomeProduto = cursor.getString(cursor.getColumnIndexOrThrow("nomeProduto"));
                    String nomeEstabelecimento = cursor.getString(cursor.getColumnIndexOrThrow("nomeEstabelecimento"));
                    String cidadeEstabelecimento = cursor.getString(cursor.getColumnIndexOrThrow("cidadeEstabelecimento"));
                    double precoVenda = cursor.getDouble(cursor.getColumnIndexOrThrow("precoVenda"));
                    String dataAtualizacao = cursor.getString(cursor.getColumnIndexOrThrow("dataAtualizacao"));

                    String dataHoraFormatada;

                    try {
                        Date data = formatoEntrada.parse(dataAtualizacao);
                        dataHoraFormatada = formatoSaida.format(data);
                    } catch (java.text.ParseException e) {
                        Log.e("BancoControle", "Erro ao formatar data, usando string bruta.", e);
                        dataHoraFormatada = dataAtualizacao;
                    }

                    String formatada = String.format(
                            "R$ %.2f | %s em %s, %s (%s)",
                            precoVenda, nomeProduto, nomeEstabelecimento, cidadeEstabelecimento, dataHoraFormatada
                    );
                    ultimasAtualizacoes.add(formatada);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao buscar últimas atualizações.", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return ultimasAtualizacoes;
    }

    public String getNomeCompletoPorEmail(String email) {
        Cursor cursor = null;
        String nomeCompleto = null;

        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        try {
            String[] campos = new String[]{"nomeCompleto"};
            String where = "email = ?";
            String[] argumentos = new String[]{email};

            cursor = db.query("Usuarios", campos, where, argumentos, null, null, null, "1");

            if (cursor != null && cursor.moveToFirst()) {
                nomeCompleto = cursor.getString(0);
            }

        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao buscar nome do usuário por email", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return nomeCompleto;
    }
}