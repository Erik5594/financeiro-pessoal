package com.eqosoftware.financeiropessoal.domain.categoria;

import com.eqosoftware.financeiropessoal.domain.common.BaseEntity;
import com.eqosoftware.financeiropessoal.domain.common.RecoverableEntity;
import com.eqosoftware.financeiropessoal.dto.categoria.TipoNatureza;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;

import java.util.List;

/**
 * Created by erik on 13/04/2022.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
public class Categoria extends BaseEntity {

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private TipoNatureza natureza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_pai")
    private Categoria categoriaPai;

    @OneToMany(mappedBy = "categoriaPai", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<Categoria> categoriasFilha;
    private int index;

}
