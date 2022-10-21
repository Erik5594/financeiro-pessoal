package com.eqosoftware.financeiropessoal.dto.conta;

public enum TipoContaBancaria {

    CORRENTE("Conta corrente"), POUPANCA("Conta poupança"), INVESTIMENT("Conta investimento");

    private String descricao;

    TipoContaBancaria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
