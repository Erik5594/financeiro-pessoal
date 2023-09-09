package com.eqosoftware.financeiropessoal.dto.dashboard;

public record TotalizadorDespesaPorSituacao(
        double totalPago,
        double totalEmAberto,
        double totalVencido,
        int qtdePago,
        int qtdeEmAberto,
        int qtdeVencido) {
}
