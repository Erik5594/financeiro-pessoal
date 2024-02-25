package com.eqosoftware.financeiropessoal.dto.despesa;

import com.eqosoftware.financeiropessoal.domain.metodopagamento.TipoMetodoPagamento;
import com.eqosoftware.financeiropessoal.dto.BaseDto;
import com.eqosoftware.financeiropessoal.dto.recorrente.RecorrenteDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Created by erik on 01/04/2022.
 */

@Data
public class DespesaDto extends BaseDto {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataLancamento;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate mesCompetencia;

    private String descricao;

    private String observacao;

    private List<DespesaCategoriaDto> categorias;

    private TipoSituacao situacao;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataVencimento;

    private UUID idMetodoPagamento;

    private TipoMetodoPagamento tipoMetodoPagamento;

    private int qtdeParcela;

    private int numParcela;

    private RecorrenteDto recorrencia;

}
