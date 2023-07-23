package com.eqosoftware.financeiropessoal.domain.erro;

public enum TipoErroDespesa implements Erro {
    NAO_ENCONTRADA("Despesa não encontrada.", 400);

    private String descricaoErro;
    private int statusHttp;

    TipoErroDespesa(String descricaoErro, int statusHttp) {
        this.descricaoErro = descricaoErro;
        this.statusHttp = statusHttp;
    }

    public String getDescricaoErro() {
        return descricaoErro;
    }

    public int getStatusHttp() {
        return statusHttp;
    }
}
