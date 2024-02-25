package com.eqosoftware.financeiropessoal.dto.despesa;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

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
