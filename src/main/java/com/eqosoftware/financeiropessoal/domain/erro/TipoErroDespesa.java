package com.eqosoftware.financeiropessoal.domain.erro;

public enum TipoErroDespesa implements Erro {

    NOME_NAO_INFORMADO("Deve ser informado um nome para a nova categoria.", 400),
    DESCRICAO_NAO_INFORMADO("Deve ser informado a descrição da nova categoria.", 400),
    NATUREZA_NAO_INFORMADO("Deve ser informado a natureza da nova categoria.", 400),
    JA_EXISTE("Já existe a categoria com o mesmo nome informado.", 400),
    NAO_ENCONTRADA("Categoria não encontrada.", 400),
    NAO_PODE_EXCLUIR_COM_FILHAS("Não é permitido excluir essa categoria, pois ela tem registro filhos.", 400);

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
