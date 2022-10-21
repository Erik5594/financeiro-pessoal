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
    Usuario toEntity(UsuarioDto source);

    @Mapping(target = "senha", ignore = true)
    @Mapping(source = "uuid", target = "id")
    UsuarioDto toDto(Usuario source);

    @IterableMapping(elementTargetType = UsuarioDto.class)
    List<UsuarioDto> toDto(List<Usuario> usuarios);

}
