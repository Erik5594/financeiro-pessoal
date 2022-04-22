package com.eqosoftware.financeiropessoal.service.usuario.mapper;

import com.eqosoftware.financeiropessoal.domain.auth.GrupoAcesso;
import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import com.eqosoftware.financeiropessoal.dto.grupoacesso.GrupoAcessoDto;
import com.eqosoftware.financeiropessoal.dto.token.UsuarioDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Created by erik on 05/04/2022.
 */

@Mapper(componentModel = "spring")
public interface GrupoAcessoMapper {

    GrupoAcessoMapper INSTANCE = Mappers.getMapper(GrupoAcessoMapper.class);

    @Mapping(source = "id", target = "uuid")
    @Mapping(target = "id", ignore = true)
    GrupoAcesso toEntity(GrupoAcessoDto source);

    @Mapping(source = "uuid", target = "id")
    GrupoAcessoDto toDto(GrupoAcesso source);

}
