package com.eqosoftware.financeiropessoal.dto.integrante;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import lombok.Data;

import java.util.UUID;

@Data
public class IntegranteDto extends BaseDto {

    private UUID usuarioId;
    private String nome;
    private TipoIntegrante tipoIntegrante;

}
