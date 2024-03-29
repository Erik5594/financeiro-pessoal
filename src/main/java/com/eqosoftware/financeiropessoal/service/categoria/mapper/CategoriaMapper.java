package com.eqosoftware.financeiropessoal.service.categoria.mapper;

import com.eqosoftware.financeiropessoal.domain.categoria.Categoria;
import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaDto;
import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaTreeDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(source = "id", target = "uuid")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "idCategoriaPai", target = "categoriaPai.uuid")
    Categoria toEntity(CategoriaDto categoriaDto);

    @Mapping(source = "uuid", target = "id")
    @Mapping(source = "categoriaPai.uuid", target = "idCategoriaPai")
    @InheritConfiguration(name = "toDto")
    CategoriaDto toDto(Categoria categoria);

    @IterableMapping(elementTargetType = CategoriaDto.class)
    List<CategoriaDto> toDtoList(List<Categoria> categorias);

    @Mapping(source = "uuid", target = "key")
    @Mapping(source = "nome", target = "title")
    @Mapping(source = "categoriasFilha", target = "children")
    CategoriaTreeDto toTree(Categoria categoria);

}
