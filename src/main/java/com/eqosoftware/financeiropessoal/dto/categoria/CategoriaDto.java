package com.eqosoftware.financeiropessoal.dto.categoria;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
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
    private UUID idCategoriaPai;
    private List<UUID> idsCategoriasFilha;

}
