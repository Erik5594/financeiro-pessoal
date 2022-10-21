package com.eqosoftware.financeiropessoal.dto.pagamento;

/**
 * Created by erik on 01/04/2022.
 */
public enum FormaPagamento {

    CARTAO_CREDITO("Cartão de Crédito"),
    CARTAO_DEBITO("Cartão de Débito"),
    BOLETO("Boleto"),
    DINHEIRO("Dinheiro"),
    PIX("PIX"),
    CHEQUE("Cheque"),
    TED("TED"),
    DOC("DOC"),
    CREDIARIO("Crediário"),
    OUTROS("Outros");

    private String descricao;

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}