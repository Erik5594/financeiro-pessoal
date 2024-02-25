package com.eqosoftware.financeiropessoal.dto.recorrente;

import com.eqosoftware.financeiropessoal.domain.recorrente.TipoFrequencia;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

public record RecorrenteRequestDto(@RequestParam(value = "uuidFormaPagamento")
                                   String uuidFormaPagamento,
                                   @RequestParam(value = "frequencia")
                                   TipoFrequencia frequencia,
                                   @RequestParam(value = "dataLimite")
                                   @JsonFormat(pattern = "dd/MM/yyyy")
                                   LocalDate dataLimite,
                                   @RequestParam(value = "primeiroLancamento")
                                   @JsonFormat(pattern = "dd/MM/yyyy")
                                   LocalDate primeiroLancamento,
                                   @RequestParam(value = "primeiroVencimento")
                                   @JsonFormat(pattern = "dd/MM/yyyy")
                                   LocalDate primeiroVencimento) {
}
