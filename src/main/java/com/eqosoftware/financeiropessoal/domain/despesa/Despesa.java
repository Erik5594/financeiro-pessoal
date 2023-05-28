package com.eqosoftware.financeiropessoal.domain.despesa;

import com.eqosoftware.financeiropessoal.domain.common.RecoverableEntity;
import com.eqosoftware.financeiropessoal.domain.metodopagamento.MetodoPagamento;
import com.eqosoftware.financeiropessoal.dto.despesa.TipoSituacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
public class Despesa extends RecoverableEntity {

    @Column(nullable = false)
    private LocalDate dataLancamento;

    @Column(nullable = false)
    private LocalDate mesCompetencia;

    @Column(nullable = false)
    private String descricao;

    private String observacao;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "despesa", cascade = CascadeType.PERSIST)
    private List<DespesaCategoria> categorias;

    @Column(nullable = false)
    private TipoSituacao situacao;

    @Column(nullable = false)
    private LocalDate dataVencimento;

    @OneToOne
    @JoinColumn(name = "id_metodo_pagamento")
    private MetodoPagamento metodoPagamento;

    private int qtdeParcela;

    private int numParcela;

    private boolean recorrente;

}
