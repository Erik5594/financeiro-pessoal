package com.eqosoftware.financeiropessoal.domain.auth;

import com.eqosoftware.financeiropessoal.domain.common.AuditableEntity;
import com.eqosoftware.financeiropessoal.domain.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

/**
 * Created by erik on 01/04/2022.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table
@Audited
public class GrupoAcesso extends AuditableEntity {

    @Column(nullable = false)
    private String nome;

    @Column
    private boolean acessoCompleto;

    @ElementCollection
    @CollectionTable(name = "grupo_acesso_roles", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "role")
    private List<String> roles;

}
