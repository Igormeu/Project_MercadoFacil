import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriarBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "MercFacil.db";
    private static final int VERSAO = 1;

    public CriarBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela 1: Endereços
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

        // Tabela 2: Produtos
        String sqlProdutos = "CREATE TABLE IF NOT EXISTS Produtos (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "quantidadePorUnidade DOUBLE, " +
                "unidadeMedida INTERGER" +
                ");";
        
        // Tabela 3: listaPreco
        String sqlListaPrecos = "CREATE TABLE IF NOT EXISTS ListaPrecos (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idEndereco INTEGER, " +
                "idProduto INTEGER, " +
                "precoVenda REAL, " +
                "dataAtualizacao TEXT, " +
                "FOREIGN KEY (idEndereco) REFERENCES Enderecos(_id), " +
                "FOREIGN KEY (idProduto) REFERENCES Produtos(_id)" +
                ");";

        // Executa as duas criações
        db.execSQL(sqlEnderecos);
        db.execSQL(sqlProdutos);
        db.execSQL(sqlListaPrecos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Caso precise atualizar a estrutura do banco:
        db.execSQL("DROP TABLE IF EXISTS Enderecos");
        db.execSQL("DROP TABLE IF EXISTS Produtos");
        onCreate(db);
    }
}
