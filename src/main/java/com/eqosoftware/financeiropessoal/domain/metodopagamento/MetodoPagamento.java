package com.eqosoftware.financeiropessoal.domain.metodopagamento;

import com.eqosoftware.financeiropessoal.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private TipoMetodoPagamento tipoMetodoPagamento;
    @Enumerated(EnumType.STRING)
    private TipoLancamentoCompetencia tipoLancamentoCompetencia;
    private Integer diaVencimento;
    private Integer diasParaFechamento;
    private Boolean padrao;

}
