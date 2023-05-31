package com.eqosoftware.financeiropessoal.domain.metodopagamento;

import com.eqosoftware.financeiropessoal.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MetodoPagamento extends BaseEntity {

    @Column(nullable = false)
    private String nome;
    @Column
    private String descricao;

}
