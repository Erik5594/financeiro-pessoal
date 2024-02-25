package com.eqosoftware.financeiropessoal.dto.recorrente;

import com.eqosoftware.financeiropessoal.domain.recorrente.TipoFrequencia;
import com.eqosoftware.financeiropessoal.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RecorrenteDto extends BaseDto {
    private TipoFrequencia frequencia;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataLimite;
    private List<PreviaRecorrenciaDto> datasRecorrencias;
}
