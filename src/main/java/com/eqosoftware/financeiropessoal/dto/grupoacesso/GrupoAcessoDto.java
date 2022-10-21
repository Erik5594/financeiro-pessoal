package com.eqosoftware.financeiropessoal.dto.grupoacesso;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Created by erik on 13/04/2022.
 */
@Data
public class GrupoAcessoDto extends BaseDto {

    private String nome;
    private boolean acessoCompleto;
    private List<String> roles;

}
