package com.eqosoftware.financeiropessoal.domain.erro;

public enum TipoErroGrupoAcesso implements Erro {

    NOME_NAO_INFORMADO("Deve ser informado o nome para o grupo de acesso.", 400),
    NOME_JA_EXISTE("Já existe um grupo de acesso cadastrado com o mesmo nome.", 400),
    NENHUMA_ROLE_INFORMADA("Deve ser informado pelo menos 1 role.", 400);

    private String descricaoErro;
    private int statusHttp;

    TipoErroGrupoAcesso(String descricaoErro, int statusHttp) {
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
