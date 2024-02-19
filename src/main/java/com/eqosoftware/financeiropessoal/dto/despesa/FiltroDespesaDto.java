package com.eqosoftware.financeiropessoal.dto.despesa;

import com.eqosoftware.financeiropessoal.domain.despesa.Despesa;
import com.eqosoftware.financeiropessoal.repository.metodopagamento.MetodoPagamentoRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

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
        LocalDate vencimento,
        String idMetodoPagamento) {



}
