package com.eqosoftware.financeiropessoal.domain.erro;

import lombok.Getter;

@Getter
public enum TipoErroDespesa implements Erro {
    NAO_ENCONTRADA("Despesa não encontrada.", 400),
    DESCRICAO_NAO_INFORMADO("Deve ser informado a descrição da despesa.", 400),
    CATEGORIA_NAO_INFORMADO("Deve ser informado pelo menos uma categoria.", 400),
    QTDE_CATEGORIA_MAIOR("Deve ser informado apenas uma categoria.", 400),
    QTDE_RECORRENCIA_MENOR("A despesa deve ter no minino 2 recorrências.", 400),
    FREQUENCIA_NAO_INFORMADA("Deve ser informado a frequência da recorrência.", 400),
    DATA_LIMITE_RECORRENCIA("Deve ser informado a data limite da recorrência.", 400),
    NUM_PARCELA_MAIOR_QUE_QTDE_PARCELA("Numero da parcela não pode ser maior que a quantidade de parcelas.", 400);

    private final String descricaoErro;
    private final int statusHttp;

    TipoErroDespesa(String descricaoErro, int statusHttp) {
        this.descricaoErro = descricaoErro;
        this.statusHttp = statusHttp;
    }

}
