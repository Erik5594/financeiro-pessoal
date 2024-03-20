package com.eqosoftware.financeiropessoal.dto.perfil;

import lombok.With;

import java.time.LocalDate;

@With
public record PerfilRequestAtualizaDto(String nome,
                                       String sobrenome,
                                       LocalDate dataNascimento,
                                       String cpf,
                                       String descricao,
                                       String username,
                                       String email) {
}
