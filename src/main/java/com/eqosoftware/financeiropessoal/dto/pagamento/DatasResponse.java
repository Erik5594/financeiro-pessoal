package com.eqosoftware.financeiropessoal.dto.pagamento;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record DatasResponse(@JsonFormat(pattern = "dd/MM/yyyy")
                            LocalDate dataVencimento,
                            @JsonFormat(pattern = "dd/MM/yyyy")
                            LocalDate dataCompetencia,
                            @JsonFormat(pattern = "dd/MM/yyyy")
                            LocalDate lancamentoAte) {
}
