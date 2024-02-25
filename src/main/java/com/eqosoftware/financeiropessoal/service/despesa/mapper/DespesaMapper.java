package com.eqosoftware.financeiropessoal.service.despesa.mapper;

import com.eqosoftware.financeiropessoal.domain.despesa.Despesa;
import com.eqosoftware.financeiropessoal.dto.despesa.DespesaDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DespesaCategoriaMapper.class, RecorrenteMapper.class})
public interface DespesaMapper {

    @Mapping(source = "id", target = "uuid")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "idMetodoPagamento", target = "metodoPagamento.uuid")
    Despesa toEntity(DespesaDto dto);

    @Mapping(source = "uuid", target = "id")
    @InheritConfiguration(name = "toDto")
    @Mapping(source = "metodoPagamento.uuid", target = "idMetodoPagamento")
    @Mapping(source = "metodoPagamento.tipoMetodoPagamento", target = "tipoMetodoPagamento")
    DespesaDto toDto(Despesa entity);

}
