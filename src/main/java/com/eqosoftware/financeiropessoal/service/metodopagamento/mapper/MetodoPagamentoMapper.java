package com.eqosoftware.financeiropessoal.service.metodopagamento.mapper;

import com.eqosoftware.financeiropessoal.domain.metodopagamento.MetodoPagamento;
import com.eqosoftware.financeiropessoal.dto.pagamento.MetodoPagamentoDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MetodoPagamentoMapper {

    @Mapping(source = "id", target = "uuid")
    @Mapping(target = "id", ignore = true)
    MetodoPagamento toEntity(MetodoPagamentoDto dto);

    @Mapping(source = "uuid", target = "id")
    @InheritConfiguration(name = "toDto")
    MetodoPagamentoDto toDto(MetodoPagamento entity);

    @IterableMapping(elementTargetType = MetodoPagamentoDto.class)
    List<MetodoPagamentoDto> toDto(List<MetodoPagamento> entitys);

}
