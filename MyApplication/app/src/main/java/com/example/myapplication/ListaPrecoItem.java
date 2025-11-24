package com.example.myapplication;

public class ListaPrecoItem {
    private String nomeProduto;
    private String localidade;
    private String dataAtualizacao;
    private double preco;
    private String cidade;
    private String marca;
    private double quantidadePorUnidade;
    private String unidadeMedida;

    public ListaPrecoItem(String nomeProduto, String localidade, String dataAtualizacao, double preco,
                          String cidade, String marca, double quantidadePorUnidade, String unidadeMedida) {
        this.nomeProduto = nomeProduto;
        this.localidade = localidade;
        this.dataAtualizacao = dataAtualizacao;
        this.preco = preco;
        this.cidade = cidade;
        this.marca = marca;
        this.quantidadePorUnidade = quantidadePorUnidade;
        this.unidadeMedida = unidadeMedida;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getDataAtualizacao() {
        return dataAtualizacao;
    }

    public double getPreco() {
        return preco;
    }

    public String getCidade() {
        return cidade;
    }

    public String getMarca() {
        return marca;
    }

    public double getQuantidadePorUnidade() {
        return quantidadePorUnidade;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }
}