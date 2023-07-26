package com.eqosoftware.financeiropessoal.domain.erro;

public enum TipoErroMetodoPagamento implements Erro {

    JA_EXISTE("Já existe o metodo pagamento com o mesmo nome informado.", 400),
    NOME_NAO_INFORMADO("Deve ser informado um nome para o novo metodo de pagamento.", 400),
    DIA_VENCIMENTO_FORA_DO_INTERVALO("Dia do vencimento deve estar entre 1 e 31.", 400),
    DIAS_FECHAMENTO_FORA_DO_INTERVALO("Dias para o fechamento deve estar entre 1 e 15.", 400);

    private String descricaoErro;
    private int statusHttp;

    TipoErroMetodoPagamento(String descricaoErro, int statusHttp) {
        this.descricaoErro = descricaoErro;
        this.statusHttp = statusHttp;
    }

    @Override
    public String getDescricaoErro() {
        return descricaoErro;
    }

    @Override
    public int getStatusHttp() {
        return statusHttp;
    }
}
