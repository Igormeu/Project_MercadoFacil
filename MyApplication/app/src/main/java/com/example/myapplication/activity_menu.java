package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class activity_menu extends AppCompatActivity {

    private View btnCadastroPreco, btnListarPrecos, btnCadastroEst, btnSobreNos;
    private Button btnLogout;
    private TextView tvBoasVindas;

    private TextView tvAtualizacao1, tvAtualizacao2;
    private BancoControle bancoControle;
    private SessaoManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        session = new SessaoManager(getApplicationContext());
        bancoControle = new BancoControle(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scroll_view_menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCadastroPreco = findViewById(R.id.buttonmeunu2);
        btnCadastroEst = findViewById(R.id.buttonmeunu3);
        btnListarPrecos = findViewById(R.id.buttonmeunu1);
        btnSobreNos = findViewById(R.id.buttonmeunu4);

        btnLogout = findViewById(R.id.btn_logout);
        tvBoasVindas = findViewById(R.id.tv_boas_vindas);

        tvAtualizacao1 = findViewById(R.id.tv_ultima_atualizacao_1);
        tvAtualizacao2 = findViewById(R.id.tv_ultima_atualizacao_2);

        btnCadastroPreco.setOnClickListener(v -> acessarCadastroPreco());
        btnCadastroEst.setOnClickListener(v -> acessarCadatroEst());
        btnListarPrecos.setOnClickListener(v -> acessarListarPrecos());
        btnSobreNos.setOnClickListener(v -> acessarSobreNos());

        btnLogout.setOnClickListener(v -> realizarLogout());
    }

    @Override
    protected void onResume() {
        super.onResume();

        bancoControle.abrirBanco();
        exibirMensagemBoasVindas();
        exibirUltimasAtualizacoes();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bancoControle.fecharBanco();
    }

    private void exibirMensagemBoasVindas() {
        String email = session.getEmailUsuario();
        String nomeUsuario = "Usuário";

        if (email != null && !email.isEmpty()) {
            if (email.contains("@")) {
                nomeUsuario = email.substring(0, email.indexOf("@"));
            } else {
                nomeUsuario = email;
            }
        }
        tvBoasVindas.setText("👋 Bem-vindo(a), " + nomeUsuario + "!");
    }

    private void exibirUltimasAtualizacoes() {
        List<String> atualizacoes = bancoControle.buscarUltimasAtualizacoesPreco();

        tvAtualizacao1.setText("");
        tvAtualizacao2.setText("");

        View icone = findViewById(R.id.image_icone_atualizacao);

        if (atualizacoes.isEmpty()) {
            tvAtualizacao1.setText("Nenhum preço registrado ainda. Seja o primeiro!");
            tvAtualizacao2.setText("");
            if (icone != null) icone.setVisibility(View.GONE);
        } else {
            if (icone != null) icone.setVisibility(View.VISIBLE);

            if (atualizacoes.size() >= 1) {
                tvAtualizacao1.setText("⭐ [AGORA] " + atualizacoes.get(0));
            }

            if (atualizacoes.size() >= 2) {
                tvAtualizacao2.setText("🕐 [ANTERIOR] " + atualizacoes.get(1));
            } else {
                tvAtualizacao2.setText("Aguardando mais registros...");
            }
        }
    }

    public void realizarLogout() {
        session.logoutUser();

        Intent i = new Intent(activity_menu.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        Toast.makeText(this, "Sessão encerrada com sucesso.", Toast.LENGTH_SHORT).show();
    }


    public void acessarCadastroPreco(){
        Intent intentCadastroPreco = new Intent(activity_menu.this, CadastrarPrecoActivity.class);
        startActivity(intentCadastroPreco);
    }

    public void acessarCadatroEst(){
        Intent intentCadastroEst = new Intent(activity_menu.this, CadastrarEstabelecimentoActivity.class);
        startActivity(intentCadastroEst);
    }

    public void acessarListarPrecos(){
        Intent intentListarPreco = new Intent(activity_menu.this, ExibirListaPrecosActivity.class);
        startActivity(intentListarPreco);
    }

    public void acessarSobreNos(){
        Intent intentSobreNos = new Intent(activity_menu.this, SobreNosActivity.class);
        startActivity(intentSobreNos);
    }
}