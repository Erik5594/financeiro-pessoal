package com.eqosoftware.financeiropessoal.dto.grupoacesso;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Created by erik on 13/04/2022.
 */
@Data
public class GrupoAcessoDto {

    private UUID id;
    private String nome;
    private boolean acessoCompleto;
    private List<String> roles;

}
