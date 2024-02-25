package com.eqosoftware.financeiropessoal.dto.pagamento;

import com.eqosoftware.financeiropessoal.domain.metodopagamento.TipoLancamentoCompetencia;
import com.eqosoftware.financeiropessoal.domain.metodopagamento.TipoMetodoPagamento;

import java.util.UUID;

public record MetodoPagamentoDto(UUID id, String nome, String descricao, TipoMetodoPagamento tipoMetodoPagamento,
                                 Integer diaVencimento, Integer diasParaFechamento, Boolean padrao,
                                 TipoLancamentoCompetencia tipoLancamentoCompetencia) {

}
