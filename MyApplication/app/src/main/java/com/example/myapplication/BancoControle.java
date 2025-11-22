package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class BancoControle {

    private SQLiteDatabase db;
    private CriarBanco banco;

    public BancoControle(Context context){
        banco = new CriarBanco(context);
    }

    // --- MÉTODOS DE CONTROLE DO BANCO ---
    public void abrirBanco() throws SQLiteException {
        // Abre o banco de dados para escrita
        db = banco.getWritableDatabase();
    }

    public void fecharBanco() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // --- MÉTODOS DE INSERÇÃO ---

    /**
     * Insere um novo endereço.
     * * ATENÇÃO: Este método agora assume que 'abrirBanco()' já foi chamado.
     */
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
        // REMOVIDO: db.close(); (Isto estava causando o erro crítico)

        if (resultado == -1)
            return "❌ Erro ao inserir endereço";
        else
            return "✅ Endereço inserido com sucesso";
    }

    /**
     * Insere um novo produto.
     * (Seguindo seu código, com a coluna 'categoria')
     */
    public String insereProduto(String nome, double quantidadePorUnidade, String unidadeMedida, String categoria) {
        ContentValues valores = new ContentValues();
        long resultado = -1;

        try {
            valores.put("nome", nome);
            valores.put("quantidadePorUnidade", quantidadePorUnidade);
            valores.put("unidadeMedida", unidadeMedida);
            valores.put("categoria", categoria);

            resultado = db.insert("Produtos", null, valores);

        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao inserir produto", e);
        }
        // REMOVIDO: db.close();

        if (resultado == -1)
            return "❌ Erro ao inserir produto";
        else
            return "✅ Produto inserido com sucesso";
    }


    /**
     * Insere uma nova lista de preços.
     */
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
        // REMOVIDO: db.close();

        if (resultado == -1)
            return "❌ Erro ao inserir lista de preço";
        else
            return "✅ Lista de preço inserida com sucesso";
    }

    /**
     * Insere um novo usuário.
     * 🚨 ALERTA DE SEGURANÇA: Salvando senha em texto puro!
     */
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
        // REMOVIDO: db.close();

        if (resultado == -1)
            return "❌ Erro ao inserir usuário";
        else
            return "✅ Usuário inserido com sucesso";
    }

    // --- MÉTODO DE CONSULTA ---

    /**
     * Carrega dados de uma tabela.
     * Assume que 'abrirBanco()' já foi chamado (ou o banco será aberto para leitura).
     * O Cursor retornado DEVE ser fechado pela Activity.
     */
    public Cursor carregaDados(String tabelaSelecionada) {
        Cursor cursor;
        String[] campos;

        // Garante que o banco está aberto (para leitura)
        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        // Definir quais campos carregar (seguindo seu código mais recente)
        switch (tabelaSelecionada) {
            case "Enderecos":
                campos = new String[] {"_id", "nomeEstab", "rede", "rua", "cidade", "bairro", "estado", "cep"};
                break;
            case "Produtos":
                // Corrigido para incluir 'categoria' e 'unidadeMedida'
                campos = new String[] {"_id", "nome", "quantidadePorUnidade", "unidadeMedida", "categoria"};
                break;
            case "ListaPrecos":
                campos = new String[] {"_id", "idEndereco", "idProduto", "precoVenda", "dataAtualizacao"};
                break;
            case "Usuarios":
                // Corrigido para incluir 'senha' (como no seu código)
                campos = new String[] {"_id","nomeCompleto", "email", "senha"};
                break;
            default:
                throw new IllegalArgumentException("Tabela inválida: " + tabelaSelecionada);
        }

        // Carregar dados da tabela selecionada
        cursor = db.query(tabelaSelecionada, campos, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        // NÃO FECHAMOS O BANCO AQUI!
        // O Cursor precisa dele aberto.

        return cursor;
    }

    /**
     * Verifica se já existe um usuário cadastrado com o login fornecido (e-mail).
     * Retorna true se o usuário for encontrado, false caso contrário.
     */
    public boolean verificaUsuarioExiste(String login) {
        Cursor cursor = null;
        boolean existe = false;

        // Garante que o banco está aberto (para leitura)
        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        try {
            String[] campos = new String[]{"email"};
            String where = "email = ?";
            String[] argumentos = new String[]{login};

            // Faz a consulta: SELECT login FROM Usuarios WHERE login = ?
            cursor = db.query("Usuarios", campos, where, argumentos, null, null, null, "1");

            // Se o cursor tem pelo menos um resultado (count > 0), o usuário existe
            if (cursor != null && cursor.getCount() > 0) {
                existe = true;
            }

        } catch (Exception e) {
            Log.e("BancoControle", "Erro ao verificar existência do usuário", e);
            // Em caso de erro, por segurança, assumimos que não existe (ou você pode lançar a exceção)
            existe = false;
        } finally {
            // É crucial fechar o cursor para liberar recursos
            if (cursor != null) {
                cursor.close();
            }
            // Nota: Não fechamos o DB aqui, pois ele é gerenciado pelo onResume/onPause da Activity.
        }

        return existe;
    }


    public boolean verificaLogin(String login, String senha) {
        Cursor cursor = null;
        boolean loginValido = false;

        // Garante que o banco está aberto (para leitura)
        if (db == null || !db.isOpen()) {
            db = banco.getReadableDatabase();
        }

        try {
            String[] campos = new String[]{"_id"}; // Basta retornar o ID, se existir
            String where = "email = ? AND senha = ?"; // Condição composta
            String[] argumentos = new String[]{login, senha};

            // Faz a consulta: SELECT _id FROM Usuarios WHERE login = ? AND senha = ?
            cursor = db.query("Usuarios", campos, where, argumentos, null, null, null, "1");

            // Se o cursor tem resultados (count > 0), o login e senha coincidem
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
}