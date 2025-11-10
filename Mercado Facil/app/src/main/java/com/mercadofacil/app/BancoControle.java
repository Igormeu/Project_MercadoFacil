public class BancoControle {

    private SQLiteDatabase db;
    private CriarBanco banco;

    public BancoControle(Context context){
        banco = new CriaBanco(context);
    }

    //Inserri endereco

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

    // Inserir Produto

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


    // Inserir Lista de Preços

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

    public String insereUsuario(String login, String senha) {
    ContentValues valores = new ContentValues();
    db = banco.getWritableDatabase();

    valores.put("login", login);
    valores.put("senha", senha);

    long resultado = db.insert("Usuarios", null, valores);
    db.close();

    if (resultado == -1)
        return "❌ Erro ao inserir usuário";
    else
        return "✅ Usuário inserido com sucesso";
    }


    public Cursor carregaDados(String tabelaSelecionada) {
    Cursor cursor;
    String[] campos;
    db = banco.getReadableDatabase();

    // Definir quais campos carregar de acordo com a tabela escolhida
    switch (tabelaSelecionada) {
        case "Enderecos":
            campos = new String[] {"_id", "nomeEstab", "rede", "rua", "cidade", "bairro", "estado", "cep"};
            break;
        case "Produtos":
            campos = new String[] {"_id", "nome", "precoPadrao", "categoria"};
            break;
        case "ListaDePrecos":
            campos = new String[] {"_id", "idEndereco", "idProduto", "precoVenda", "dataAtualizacao"};
            break;
        case "sqlUsuarios":
            campos = new String[] {"_id", "login", "senha"};
            break;
        default:
            throw new IllegalArgumentException("Tabela inválida: " + tabelaSelecionada);
    }

    // Carregar dados da tabela selecionada
    cursor = db.query(tabelaSelecionada, campos, null, null, null, null, null);

    if (cursor != null) {
        cursor.moveToFirst();  // Move para o primeiro item, caso haja dados
    }
    db.close();
    return cursor;
    }

}