package com.eqosoftware.financeiropessoal.dto.token;

import lombok.Data;

import java.util.UUID;

/**
 * Created by erik on 28/01/2022.
 */

@Data
public class UsuarioDto {

    private UUID id;
    private String nome;
    private String username;
    private String email;
    private String senha;

}
