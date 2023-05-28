package com.eqosoftware.financeiropessoal.service.despesa.mapper;

import com.eqosoftware.financeiropessoal.domain.despesa.DespesaCategoria;
import com.eqosoftware.financeiropessoal.dto.despesa.DespesaCategoriaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DespesaCategoriaMapper {

    @Mapping(source = "idCategoria", target = "categoria.uuid")
    DespesaCategoria toEntity(DespesaCategoriaDto dto);

    @Mapping(source = "categoria.uuid", target = "idCategoria")
    DespesaCategoriaDto toDto(DespesaCategoria entity);

}
