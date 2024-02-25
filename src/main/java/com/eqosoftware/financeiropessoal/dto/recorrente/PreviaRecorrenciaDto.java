package com.eqosoftware.financeiropessoal.dto.recorrente;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record PreviaRecorrenciaDto(Integer id,
                                   @JsonFormat(pattern = "dd/MM/yyyy")
                                   LocalDate competencia,
                                   @JsonFormat(pattern = "dd/MM/yyyy")
                                   LocalDate lancamento,
                                   @JsonFormat(pattern = "dd/MM/yyyy")
                                   LocalDate vencimento) {
}
