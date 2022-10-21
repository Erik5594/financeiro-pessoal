package com.eqosoftware.financeiropessoal.dto.pagamento;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by erik on 01/04/2022.
 */

@Data
public class ParcelamentoDto {

    private int qtdeParcelas;
    private LocalDate vencimentoPrimeira;
    private LocalDate vencimentoUltima;
    private List<ParcelaDto> parcelas;

}
