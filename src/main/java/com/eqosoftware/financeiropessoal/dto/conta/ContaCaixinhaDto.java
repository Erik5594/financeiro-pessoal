package com.eqosoftware.financeiropessoal.dto.conta;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ContaCaixinhaDto extends BaseDto {

    private UUID id;
    private BigDecimal saldoInicial;
    private LocalDate dataSaldo;

}
