package com.eqosoftware.financeiropessoal.dto.pagamento;

import lombok.Data;

import java.time.LocalDate;

/**
 * Created by erik on 01/04/2022.
 */
@Data
public class ParcelaDto {

    private int numero;
    private Double valor;
    private LocalDate dataVencimento;
    private Boolean pago;
    private LocalDate dataPagamento;

}
