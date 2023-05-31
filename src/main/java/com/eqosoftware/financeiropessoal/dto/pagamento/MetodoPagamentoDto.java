package com.eqosoftware.financeiropessoal.dto.pagamento;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import lombok.Data;

@Data
public class MetodoPagamentoDto extends BaseDto {

    private String nome;
    private String descricao;

}
