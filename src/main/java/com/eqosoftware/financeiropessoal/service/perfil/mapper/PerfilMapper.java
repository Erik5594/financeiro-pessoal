package com.eqosoftware.financeiropessoal.service.perfil.mapper;

import com.eqosoftware.financeiropessoal.domain.perfil.Perfil;
import com.eqosoftware.financeiropessoal.dto.perfil.PerfilDto;
import com.eqosoftware.financeiropessoal.dto.perfil.PerfilRequestAtualizaDto;
import com.eqosoftware.financeiropessoal.service.usuario.mapper.UsuarioMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface PerfilMapper {

    PerfilDto toDto(Perfil entity);
    @Mapping(ignore = true, target = "usuario")
    PerfilDto toDtoAux(PerfilRequestAtualizaDto dto);
    Perfil toEntity(PerfilDto dto);

}
