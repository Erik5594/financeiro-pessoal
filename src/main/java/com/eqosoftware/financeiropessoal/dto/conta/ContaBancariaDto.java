package com.eqosoftware.financeiropessoal.dto.conta;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ContaBancariaDto extends BaseDto {

    private UUID idBanco;
    private TipoContaBancaria tipoConta;
    private String agencia;
    private String digitoAgencia;
    private String numeroConta;
    private String digitoConta;
    private BigDecimal saldoInicial;
    private LocalDate dataSaldo;

}
