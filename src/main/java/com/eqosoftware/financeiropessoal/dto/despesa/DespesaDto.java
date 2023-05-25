package com.eqosoftware.financeiropessoal.dto.despesa;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import com.eqosoftware.financeiropessoal.dto.pagamento.PagamentoDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Created by erik on 01/04/2022.
 */

@Data
public class DespesaDto extends BaseDto {

    private LocalDate dataLancamento;
    private LocalDate mesCompetencia;
    private String descricao;
    private String observacao;
    private List<DespesaCategoriaDto> categorias;
    private TipoSituacao situacao;
    private LocalDate dataVencimento;
    private UUID idMetodoPagamento;
    private int qtdeParcela;

}
