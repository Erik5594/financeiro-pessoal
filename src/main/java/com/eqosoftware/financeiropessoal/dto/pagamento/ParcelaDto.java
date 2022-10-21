package com.eqosoftware.financeiropessoal.dto.pagamento;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import lombok.Data;

import java.time.LocalDate;

/**
 * Created by erik on 01/04/2022.
 */
@Data
public class ParcelaDto extends BaseDto {

    private int numero;
    private Double valor;
    private LocalDate dataVencimento;
    private Boolean pago;
    private LocalDate dataPagamento;

}
