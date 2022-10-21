package com.eqosoftware.financeiropessoal.dto.pagamento;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by erik on 01/04/2022.
 */

@Data
public class ParcelamentoDto extends BaseDto {

    private int qtdeParcelas;
    private LocalDate vencimentoPrimeira;
    private LocalDate vencimentoUltima;
    private List<ParcelaDto> parcelas;

}
