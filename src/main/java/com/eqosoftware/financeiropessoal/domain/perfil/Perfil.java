package com.eqosoftware.financeiropessoal.domain.perfil;

import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import com.eqosoftware.financeiropessoal.domain.common.RecoverableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Perfil extends RecoverableEntity {

    private String nome;
    private String sobrenome;
    @Temporal(TemporalType.DATE)
    private LocalDate dataNascimento;
    private String cpf;
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    private String descricao;
    private String urlImagemPerfil;

}
