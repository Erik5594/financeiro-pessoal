package com.eqosoftware.financeiropessoal.dto.conta;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ContaDto extends BaseDto {

    private TipoConta tipoConta;
    private String descricao;
    private ContaBancariaDto contaBancaria;
    private ContaCaixinhaDto contaCaixinha;
    private ContaCartaoDto contaCartao;
    private BigDecimal saldoAtual;
    private BigDecimal saldoFuturo;

}
