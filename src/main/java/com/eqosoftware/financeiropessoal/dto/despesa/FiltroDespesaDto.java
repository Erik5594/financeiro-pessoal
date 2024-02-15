package com.eqosoftware.financeiropessoal.dto.despesa;

import com.eqosoftware.financeiropessoal.domain.despesa.Despesa;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Objects;

public record FiltroDespesaDto(
        @RequestParam(value = "descricao", required = false)
        String descricao,
        @RequestParam(value = "tipoSituacao", required = false)
        TipoSituacao tipoSituacao,
        @RequestParam(value = "competencia")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate competencia,
        @RequestParam(value = "vencimento", required = false)
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate vencimento) {

        public Specification<Despesa> toSpec(){
                return (root, query, builder) -> {
                        var predicados = new ArrayList<Predicate>();
                        if(Objects.nonNull(this.competencia)){
                                var dataInicio = competencia.with(TemporalAdjusters.firstDayOfMonth());
                                var dataFim = competencia.with(TemporalAdjusters.lastDayOfMonth());
                                predicados.add(builder.between(root.get("mesCompetencia"), dataInicio, dataFim));
                        }
                        if(StringUtils.isNotBlank(this.descricao)){
                                predicados.add(builder.like(builder.lower(root.get("descricao")), "%"+this.descricao+"%"));
                        }
                        if(Objects.nonNull(this.tipoSituacao)){
                                switch (this.tipoSituacao){
                                        case PARCIALMENTE_PAGO, PAGO -> {
                                                predicados.add(builder.equal(root.get("situacao"), tipoSituacao));
                                                if(Objects.nonNull(this.vencimento)){
                                                        predicados.add(builder.equal(root.get("dataVencimento"), this.vencimento));
                                                }
                                        }
                                        case VENCIDA -> {
                                                var dataVencimento = Objects.isNull(this.vencimento) || LocalDate.now().isBefore(this.vencimento) ? LocalDate.now() : this.vencimento;
                                                predicados.add(builder.equal(root.get("situacao"), TipoSituacao.EM_ABERTO));
                                                predicados.add(builder.lessThan(root.get("dataVencimento"), dataVencimento));
                                        }
                                        case EM_ABERTO -> {
                                                var dataVencimento = Objects.isNull(this.vencimento) || this.vencimento.isBefore(LocalDate.now()) ? LocalDate.now() : this.vencimento;
                                                predicados.add(builder.equal(root.get("situacao"), tipoSituacao));
                                                predicados.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"), dataVencimento));
                                        }
                                }
                        }else{
                                if(Objects.nonNull(this.vencimento)){
                                        predicados.add(builder.equal(root.get("dataVencimento"), this.vencimento));
                                }
                        }
                        return builder.and(predicados.toArray(Predicate[]::new));
                };
        }

}
