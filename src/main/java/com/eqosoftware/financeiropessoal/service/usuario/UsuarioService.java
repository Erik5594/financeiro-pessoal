package com.eqosoftware.financeiropessoal.service.usuario;

import com.eqosoftware.financeiropessoal.domain.auth.GrupoAcesso;
import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import com.eqosoftware.financeiropessoal.dto.grupoacesso.GrupoAcessoDto;
import com.eqosoftware.financeiropessoal.dto.token.UsuarioDto;
import com.eqosoftware.financeiropessoal.repository.usuario.GrupoAcessoRepository;
import com.eqosoftware.financeiropessoal.repository.usuario.UsuarioRepository;
import com.eqosoftware.financeiropessoal.service.usuario.mapper.GrupoAcessoMapper;
import com.eqosoftware.financeiropessoal.service.usuario.mapper.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by erik on 28/01/2022.
 */

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private GrupoAcessoRepository grupoAcessoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioMapper usuarioMapper;
    @Autowired
    private GrupoAcessoMapper grupoAcessoMapper;
    public Usuario buscarByEmailOrUsername(String valor){
        return usuarioRepository.findUsuarioByEmailOrUsername(valor ,valor);
    }

    public UsuarioDto criarUsuario(UsuarioDto usuarioDto){
        Usuario usuario = usuarioRepository.save(mapperToEntityValuesDefaults(usuarioDto));
        return usuarioMapper.toDto(usuario);
    }

    private Usuario mapperToEntityValuesDefaults(UsuarioDto usuarioDto){
        usuarioDto.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));
        Usuario usuario = usuarioMapper.toEntity(usuarioDto);
        usuario.setAtivo(true);
        usuario.setBloqueado(false);
        usuario.setGrupoAcesso(grupoAcessoRepository.findById(1l).orElse(null));
        return usuario;
    }

    public GrupoAcessoDto criarGrupoAcesso(GrupoAcessoDto grupoAcessoDto){
        GrupoAcesso grupoAcesso = grupoAcessoMapper.toEntity(grupoAcessoDto);
        grupoAcesso.setCreatedBy("system");
        grupoAcessoRepository.save(grupoAcesso);
        return grupoAcessoMapper.toDto(grupoAcesso);
    }

    public List<GrupoAcessoDto> listarGrupoAcesso(){
        return grupoAcessoRepository.findAll().stream()
                .map(grupoAcessoMapper::toDto).collect(Collectors.toList());
    }

}
