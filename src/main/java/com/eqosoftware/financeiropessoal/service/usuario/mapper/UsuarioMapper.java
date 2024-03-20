package com.eqosoftware.financeiropessoal.service.usuario.mapper;

import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import com.eqosoftware.financeiropessoal.dto.token.UsuarioDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created by erik on 05/04/2022.
 */

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(source = "id", target = "uuid")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "tenant", target = "tenant.nomeSchema")
    Usuario toEntity(UsuarioDto source);

    @Mapping(target = "senha", ignore = true)
    @Mapping(source = "uuid", target = "id")
    @Mapping(source = "tenant.nomeSchema", target = "tenant")
    UsuarioDto toDto(Usuario source);

    @IterableMapping(elementTargetType = UsuarioDto.class)
    List<UsuarioDto> toDto(List<Usuario> usuarios);

}
