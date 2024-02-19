package com.eqosoftware.financeiropessoal.dto.despesa;

import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DespesaCategoriaDto {

    private UUID idCategoria;
    private CategoriaDto categoria;
    private String descricao;
    private BigDecimal valor;

}
