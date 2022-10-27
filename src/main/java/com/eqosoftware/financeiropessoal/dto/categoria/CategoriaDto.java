package com.eqosoftware.financeiropessoal.dto.categoria;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Created by erik on 01/04/2022.
 */

@Data
public class CategoriaDto extends BaseDto {

    private String nome;
    private String descricao;
    private NaturezaDto natureza;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID idCategoriaPai;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<UUID> idsCategoriasFilha;

}
