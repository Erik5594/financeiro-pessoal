package com.eqosoftware.financeiropessoal.domain.erro;

import lombok.Getter;

@Getter
public enum TipoErroCategoria implements Erro {

    NOME_NAO_INFORMADO("Deve ser informado um nome para a nova categoria.", 400),
    DESCRICAO_NAO_INFORMADO("Deve ser informado a descrição da categoria.", 400),
    NATUREZA_NAO_INFORMADO("Deve ser informado a natureza da nova categoria.", 400),
    JA_EXISTE("Já existe a categoria com o mesmo nome informado.", 400),
    NAO_ENCONTRADA("Categoria não encontrada.", 400),
    INDEX_MAX("Nivel máximo de sub-categoria atingido.", 400),
    NAO_PODE_EXCLUIR_COM_FILHAS("Não é permitido excluir essa categoria, pois ela tem registro filhos.", 400);

    private final String descricaoErro;
    private final int statusHttp;

    TipoErroCategoria(String descricaoErro, int statusHttp) {
        this.descricaoErro = descricaoErro;
        this.statusHttp = statusHttp;
    }

}
