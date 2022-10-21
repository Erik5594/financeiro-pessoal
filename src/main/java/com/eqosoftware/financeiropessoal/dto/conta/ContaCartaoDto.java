package com.eqosoftware.financeiropessoal.dto.conta;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import lombok.Data;

import java.util.UUID;

@Data
public class ContaCartaoDto extends BaseDto {

    private String ultimosDigitos;
    private int diaFechamento;
    private int diaVencimento;
    private String bandeira;
    private UUID idConta;

}
