package com.eqosoftware.financeiropessoal.domain.despesa;

import com.eqosoftware.financeiropessoal.domain.common.RecoverableEntity;
import com.eqosoftware.financeiropessoal.domain.metodopagamento.MetodoPagamento;
import com.eqosoftware.financeiropessoal.domain.recorrente.Recorrente;
import com.eqosoftware.financeiropessoal.dto.despesa.TipoSituacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Where(clause = RecoverableEntity.NOT_DELETED)
public class Despesa extends RecoverableEntity {

    @Column(nullable = false)
    private LocalDate dataLancamento;
    @Column(nullable = false)
    private LocalDate mesCompetencia;
    @Column(nullable = false)
    private String descricao;
    private String observacao;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "despesa", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
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
    @ManyToOne
    @JoinColumn(name = "id_recorrente")
    private Recorrente recorrencia;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "despesa_pai")
    private Despesa despesaPai;

}
