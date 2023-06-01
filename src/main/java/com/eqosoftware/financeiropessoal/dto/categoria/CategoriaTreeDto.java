package com.eqosoftware.financeiropessoal.dto.categoria;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CategoriaTreeDto {

    private UUID key;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CategoriaTreeDto> children;

}
