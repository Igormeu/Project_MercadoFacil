package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;


public class ListaPrecoAdapter extends RecyclerView.Adapter<ListaPrecoAdapter.ViewHolder> {

    private List<ListaPrecoItem> listaOriginal;
    private List<ListaPrecoItem> listaFiltrada;

    public ListaPrecoAdapter(List<ListaPrecoItem> listaCompleta) {
        this.listaOriginal = listaCompleta;
        this.listaFiltrada = new ArrayList<>(listaCompleta);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_buscar_preco, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListaPrecoItem item = listaFiltrada.get(position);

        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String precoFormatado = formatadorMoeda.format(item.getPreco());


        String qtdUnidadeFormatada = String.format(Locale.getDefault(),
                "(%.1f %s)",
                item.getQuantidadePorUnidade(),
                item.getUnidadeMedida());


        String cidade = item.getCidade();
        String estabelecimentoRua = item.getLocalidade();

        SpannableStringBuilder localidadeFormatada = new SpannableStringBuilder();
        localidadeFormatada.append(cidade);
        localidadeFormatada.setSpan(new StyleSpan(Typeface.BOLD), 0, cidade.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        localidadeFormatada.append(", ").append(estabelecimentoRua);


        holder.tvNomeProduto.setText(item.getNomeProduto());
        holder.tvMarca.setText(item.getMarca());
        holder.tvQtdUnidade.setText(qtdUnidadeFormatada);
        holder.tvLocalidade.setText(localidadeFormatada);
        holder.tvAtualizacao.setText(item.getDataAtualizacao());
        holder.tvPreco.setText(precoFormatado);
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeProduto;
        TextView tvLocalidade;
        TextView tvAtualizacao;
        TextView tvPreco;
        TextView tvMarca;
        TextView tvQtdUnidade;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNomeProduto = itemView.findViewById(R.id.tv_produto_nome);
            tvLocalidade = itemView.findViewById(R.id.tv_localidade);
            tvAtualizacao = itemView.findViewById(R.id.tv_atualizacao);
            tvPreco = itemView.findViewById(R.id.tv_preco);
            tvMarca = itemView.findViewById(R.id.tv_marca);
            tvQtdUnidade = itemView.findViewById(R.id.tv_produto_quantidade_unidade);
        }
    }

    public void filtrar(String nomeProdutoQuery, String cidadeSelecionada) {
        listaFiltrada.clear();

        String nomePattern = nomeProdutoQuery.toLowerCase().trim();
        boolean filtrarPorCidade = !cidadeSelecionada.equals("Todas as Cidades");

        if (nomePattern.isEmpty() && !filtrarPorCidade) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            for (ListaPrecoItem item : listaOriginal) {
                boolean matchProduto = item.getNomeProduto().toLowerCase().contains(nomePattern);
                boolean matchCidade = !filtrarPorCidade || item.getCidade().equals(cidadeSelecionada);

                if (matchProduto && matchCidade) {
                    listaFiltrada.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }
}
