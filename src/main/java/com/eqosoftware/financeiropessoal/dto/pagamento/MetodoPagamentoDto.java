package com.eqosoftware.financeiropessoal.dto.pagamento;

import java.util.UUID;

public record MetodoPagamentoDto(UUID id, String nome, String descricao, Integer diaVencimento, Integer diasParaFechamento) {

}
