package com.eqosoftware.financeiropessoal.domain.despesa;

import com.eqosoftware.financeiropessoal.domain.categoria.Categoria;
import com.eqosoftware.financeiropessoal.domain.common.RecoverableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
public class DespesaCategoria extends RecoverableEntity {

    @OneToOne
    private Categoria categoria;

    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_despesa")
    private Despesa despesa;

}
