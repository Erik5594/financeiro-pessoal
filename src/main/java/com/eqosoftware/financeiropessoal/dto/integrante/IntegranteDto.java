package com.eqosoftware.financeiropessoal.dto.integrante;

import lombok.Data;

import java.util.UUID;

@Data
public class IntegranteDto {

    private UUID id;
    private UUID usuarioId;
    private String nome;
    private TipoIntegrante tipoIntegrante;

}
