package com.eqosoftware.financeiropessoal.domain.auth;

import com.eqosoftware.financeiropessoal.domain.common.RecoverableEntity;
import com.eqosoftware.financeiropessoal.domain.tenant.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Created by erik on 28/01/2022.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(schema = "public")
public class Usuario extends RecoverableEntity {

    @Column(nullable = false, length = 100, updatable = false)
    private String username;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false)
    private boolean bloqueado;

    @JoinColumn(name = "grupo_acesso_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private GrupoAcesso grupoAcesso;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

}
