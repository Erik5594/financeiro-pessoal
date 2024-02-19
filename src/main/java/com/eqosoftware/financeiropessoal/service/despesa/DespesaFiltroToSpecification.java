package com.eqosoftware.financeiropessoal.service.despesa;

import com.eqosoftware.financeiropessoal.domain.despesa.Despesa;
import com.eqosoftware.financeiropessoal.dto.despesa.FiltroDespesaDto;
import com.eqosoftware.financeiropessoal.dto.despesa.TipoSituacao;
import com.eqosoftware.financeiropessoal.repository.metodopagamento.MetodoPagamentoRepository;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Component
public class DespesaFiltroToSpecification {

    private final MetodoPagamentoRepository metodoPagamentoRepository;

    @Autowired
    public DespesaFiltroToSpecification(MetodoPagamentoRepository metodoPagamentoRepository) {
        this.metodoPagamentoRepository = metodoPagamentoRepository;
    }

    public Specification<Despesa> toSpecification(FiltroDespesaDto filtro){
        return (root, query, builder) -> {
            var predicados = new ArrayList<Predicate>();
            if(Objects.nonNull(filtro.competencia())){
                var dataInicio = filtro.competencia().with(TemporalAdjusters.firstDayOfMonth());
                var dataFim = filtro.competencia().with(TemporalAdjusters.lastDayOfMonth());
                predicados.add(builder.between(root.get("mesCompetencia"), dataInicio, dataFim));
            }
            if(StringUtils.isNotBlank(filtro.descricao())){
                predicados.add(builder.like(builder.lower(root.get("descricao")), "%"+filtro.descricao()+"%"));
            }
            if(Objects.nonNull(filtro.tipoSituacao())){
                switch (filtro.tipoSituacao()){
                    case PARCIALMENTE_PAGO, PAGO -> {
                        predicados.add(builder.equal(root.get("situacao"), filtro.tipoSituacao()));
                        if(Objects.nonNull(filtro.vencimento())){
                            predicados.add(builder.equal(root.get("dataVencimento"), filtro.vencimento()));
                        }
                    }
                    case VENCIDA -> {
                        var dataVencimento = Objects.isNull(filtro.vencimento()) || LocalDate.now().isBefore(filtro.vencimento()) ? LocalDate.now() : filtro.vencimento();
                        predicados.add(builder.equal(root.get("situacao"), TipoSituacao.EM_ABERTO));
                        predicados.add(builder.lessThan(root.get("dataVencimento"), dataVencimento));
                    }
                    case EM_ABERTO -> {
                        var dataVencimento = Objects.isNull(filtro.vencimento()) || filtro.vencimento().isBefore(LocalDate.now()) ? LocalDate.now() : filtro.vencimento();
                        predicados.add(builder.equal(root.get("situacao"), filtro.tipoSituacao()));
                        predicados.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"), dataVencimento));
                    }
                }
            }else{
                if(Objects.nonNull(filtro.vencimento())){
                    predicados.add(builder.equal(root.get("dataVencimento"), filtro.vencimento()));
                }
            }
            if(StringUtils.isNotBlank(filtro.idMetodoPagamento())){
                var metodoPagamentoOpt = metodoPagamentoRepository.findByUuid(UUID.fromString(filtro.idMetodoPagamento()));
                metodoPagamentoOpt.ifPresent(metodoPagamento -> predicados.add(builder.equal(root.get("metodoPagamento"), metodoPagamento)));
            }
            return builder.and(predicados.toArray(Predicate[]::new));
        };
    }
}
