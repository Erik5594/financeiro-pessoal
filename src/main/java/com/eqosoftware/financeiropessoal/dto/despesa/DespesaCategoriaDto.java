package com.eqosoftware.financeiropessoal.dto.despesa;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DespesaCategoriaDto {

    private UUID idCategoria;
    private BigDecimal valor;

}
