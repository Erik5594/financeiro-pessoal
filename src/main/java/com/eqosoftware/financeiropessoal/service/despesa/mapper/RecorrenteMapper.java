package com.eqosoftware.financeiropessoal.service.despesa.mapper;

import com.eqosoftware.financeiropessoal.domain.recorrente.Recorrente;
import com.eqosoftware.financeiropessoal.dto.recorrente.RecorrenteDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecorrenteMapper {

    @Mapping(source = "id", target = "uuid")
    @Mapping(target = "id", ignore = true)
    Recorrente toEntity(RecorrenteDto dto);

    @Mapping(source = "uuid", target = "id")
    @InheritConfiguration(name = "toDto")
    RecorrenteDto toDto(Recorrente entity);

}
