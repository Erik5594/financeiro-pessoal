package com.eqosoftware.financeiropessoal.dto.despesa;

import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaDto;
import com.eqosoftware.financeiropessoal.dto.pagamento.Pagamento;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Created by erik on 01/04/2022.
 */

@Data
public class DespesaDto {

    private UUID id;
    private LocalDate dataLancamento;
    private LocalDate dataGeracao;
    private LocalDate mesCompetencia;
    private String descricao;
    private String observacao;
    private Double valor;
    private CategoriaDto categoria;
    private Situacao situacao;
    private LocalDate dataVencimento;
    private List<Pagamento> pagamentos;

}
