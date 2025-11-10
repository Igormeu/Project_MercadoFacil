public class BancoControle {

    private SQLiteDatabase db;
    private CriarBanco banco;

    public BancoControle(Context context){
        banco = new CriaBanco(context);
    }

    public String insereEndereco(String nomeEstab, String rede, String rua,
                                 String cidade, String bairro, String estado, String cep) {
        ContentValues valores = new ContentValues();
        db = banco.getWritableDatabase();

        valores.put("nomeEstab", nomeEstab);
        valores.put("rede", rede);
        valores.put("rua", rua);
        valores.put("cidade", cidade);
        valores.put("bairro", bairro);
        valores.put("estado", estado);
        valores.put("cep", cep);

        long resultado = db.insert("Enderecos", null, valores);
        db.close();

        if (resultado == -1)
            return "❌ Erro ao inserir endereço";
        else
            return "✅ Endereço inserido com sucesso";
    }

    // ==============================
    // 2️⃣ Inserir Produto
    // ==============================
    public String insereProduto(String nome, double quantidadePorUnidade, int unidadeMedida) {
        ContentValues valores = new ContentValues();
        db = banco.getWritableDatabase();

        valores.put("nome", nome);
        valores.put("quantidadePorUnidade", quantidadePorUnidade);
        valores.put("unidadeMedida", unidadeMedida);

        long resultado = db.insert("Produtos", null, valores);
        db.close();

        if (resultado == -1)
            return "❌ Erro ao inserir produto";
        else
            return "✅ Produto inserido com sucesso";
    }

    // ==============================
    // 3️⃣ Inserir Lista de Preços
    // ==============================
    public String insereListaPreco(int idEndereco, int idProduto, double precoVenda, String dataAtualizacao) {
        ContentValues valores = new ContentValues();
        db = banco.getWritableDatabase();

        valores.put("idEndereco", idEndereco);
        valores.put("idProduto", idProduto);
        valores.put("precoVenda", precoVenda);
        valores.put("dataAtualizacao", dataAtualizacao);

        long resultado = db.insert("ListaDePrecos", null, valores);
        db.close();

        if (resultado == -1)
            return "❌ Erro ao inserir lista de preço";
        else
            return "✅ Lista de preço inserida com sucesso";
    }
}