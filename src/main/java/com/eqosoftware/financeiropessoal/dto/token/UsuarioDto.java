package com.eqosoftware.financeiropessoal.dto.token;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

/**
 * Created by erik on 28/01/2022.
 */

@Data
public class UsuarioDto extends BaseDto {

    private String nome;
    private String username;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String senha;

}
