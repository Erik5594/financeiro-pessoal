package com.eqosoftware.financeiropessoal.dto.conta;

public enum TipoConta {

    CORRENTE("Conta corrente"), POUPANCA("Conta poupança"), INVESTIMENTO("Conta investimento"), CAIXINHA("Carteira/Em mãos"), CARTAO("Cartão de crédito");

    private String descricao;

    TipoConta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
