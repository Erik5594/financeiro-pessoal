package com.eqosoftware.financeiropessoal.domain.auth;

import com.eqosoftware.financeiropessoal.domain.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

/**
 * Created by erik on 01/04/2022.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(schema = "public")
public class GrupoAcesso extends AuditableEntity {

    @Column(nullable = false)
    private String nome;

    @Column
    private boolean acessoCompleto;

    @ElementCollection
    @CollectionTable(name = "grupo_acesso_roles", schema = "public", joinColumns = @JoinColumn(name = "id_grupo_acesso"))
    @Column(name = "role")
    private List<String> roles;

}
