package com.eqosoftware.financeiropessoal.dto.categoria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaNaturezaTreeDto {

    private List<CategoriaTreeDto> receitas;
    private List<CategoriaTreeDto> despesas;

}
