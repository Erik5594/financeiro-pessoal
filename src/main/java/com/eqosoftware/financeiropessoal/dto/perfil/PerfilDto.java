package com.eqosoftware.financeiropessoal.dto.perfil;

import com.eqosoftware.financeiropessoal.dto.token.UsuarioDto;
import lombok.With;

import java.time.LocalDate;

@With
public record PerfilDto(String nome,
                        String sobrenome,
                        LocalDate dataNascimento,
                        String cpf,
                        String descricao,
                        String urlImagemPerfil,
                        UsuarioDto usuario) {
}
