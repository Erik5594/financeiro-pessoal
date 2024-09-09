package com.eqosoftware.financeiropessoal.dto.pagamento;

import com.eqosoftware.financeiropessoal.domain.metodopagamento.TipoLancamentoCompetencia;
import com.eqosoftware.financeiropessoal.domain.metodopagamento.TipoMetodoPagamento;
import lombok.With;

import java.util.UUID;

@With
public record MetodoPagamentoDto(UUID id, String nome, String descricao, TipoMetodoPagamento tipoMetodoPagamento,
                                 Integer diaVencimento, Integer diasParaFechamento, Boolean padrao,
                                 TipoLancamentoCompetencia tipoLancamentoCompetencia, Boolean despesaVinculada) {

}
