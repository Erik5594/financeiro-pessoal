package com.eqosoftware.financeiropessoal.service.usuario;

import com.eqosoftware.financeiropessoal.domain.auth.GrupoAcesso;
import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroGrupoAcesso;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroUsuario;
import com.eqosoftware.financeiropessoal.dto.grupoacesso.GrupoAcessoDto;
import com.eqosoftware.financeiropessoal.dto.token.UsuarioDto;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
import com.eqosoftware.financeiropessoal.repository.usuario.GrupoAcessoRepository;
import com.eqosoftware.financeiropessoal.repository.usuario.UsuarioRepository;
import com.eqosoftware.financeiropessoal.service.tenant.TenantService;
import com.eqosoftware.financeiropessoal.service.usuario.mapper.GrupoAcessoMapper;
import com.eqosoftware.financeiropessoal.service.usuario.mapper.UsuarioMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by erik on 28/01/2022.
 */

@Slf4j
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
    @Autowired
    private TenantService tenantService;

    public Usuario buscarByEmailOrUsername(String valor){
        return usuarioRepository.findUsuarioByEmailOrUsername(valor ,valor);
    }

    @Transactional
    public UsuarioDto criarUsuario(UsuarioDto usuarioDto){
        validarNovoUsuario(usuarioDto);
        var tenant = tenantService.criarTenant(usuarioDto.getUsername());
        var grupoAcesso = grupoAcessoRepository.findById(1L).orElse(null);
        var usuario = mapperToEntityValuesDefaults(usuarioDto);
        usuario.setGrupoAcesso(grupoAcesso);
        usuario.setCreatedBy(usuario.getUsername());
        usuario.setTenant(tenant);
        return usuarioMapper.toDto(usuarioRepository.saveAndFlush(usuario));
    }

    private void validarUsername(@NonNull final UsuarioDto usuarioDto){
        if(StringUtils.isBlank(usuarioDto.getUsername())){
            throw new ValidacaoException(TipoErroUsuario.USERNAME_NAO_INFORMADO);
        }

        if(jaExisteUsername(usuarioDto.getUsername())){
            throw new ValidacaoException(TipoErroUsuario.USERNAME_JA_EXISTE);
        }
    }

    private void validarNovoUsuario(@NonNull final UsuarioDto usuarioDto){

        validarUsername(usuarioDto);

        if(StringUtils.isBlank(usuarioDto.getNome())){
            throw new ValidacaoException(TipoErroUsuario.NOME_NAO_INFORMADO);
        }

        if(StringUtils.isBlank(usuarioDto.getEmail())){
            throw new ValidacaoException(TipoErroUsuario.EMAIL_NAO_INFORMADO);
        }

        if(StringUtils.isBlank(usuarioDto.getSenha())){
            throw new ValidacaoException(TipoErroUsuario.SENHA_NAO_INFORMADO);
        }

        if(usuarioDto.getSenha().length() < 6){
            throw new ValidacaoException(TipoErroUsuario.SENHA_CARACTERES_MINIMO_NAO_ATINGIDO);
        }

        if(!usuarioDto.getEmail().contains("@")){
            throw new ValidacaoException(TipoErroUsuario.EMAIL_INVALIDO);
        }

        if(jaExisteEmail(usuarioDto.getEmail())){
            throw new ValidacaoException(TipoErroUsuario.EMAIL_JA_EXISTE);
        }

    }

    private boolean jaExisteEmail(@NonNull final String email){
        return buscarByEmailOrUsername(email) != null;
    }

    private boolean jaExisteUsername(@NonNull final String username){
        return buscarByEmailOrUsername(username) != null;
    }

    private Usuario mapperToEntityValuesDefaults(UsuarioDto usuarioDto){
        usuarioDto.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));
        Usuario usuario = usuarioMapper.toEntity(usuarioDto);
        usuario.setAtivo(true);
        usuario.setBloqueado(false);
        usuario.setCreatedBy("system");
        usuario.setGrupoAcesso(grupoAcessoRepository.findById(1L).orElse(null));
        return usuario;
    }

    public GrupoAcessoDto criarGrupoAcesso(GrupoAcessoDto grupoAcessoDto){
        validarNovoGrupoAcesso(grupoAcessoDto);
        GrupoAcesso grupoAcesso = grupoAcessoMapper.toEntity(grupoAcessoDto);
        grupoAcesso.setCreatedBy("system");
        grupoAcessoRepository.save(grupoAcesso);
        return grupoAcessoMapper.toDto(grupoAcesso);
    }

    private void validarNovoGrupoAcesso(GrupoAcessoDto grupoAcessoDto){
        if(StringUtils.isBlank(grupoAcessoDto.getNome())){
            throw new ValidacaoException(TipoErroGrupoAcesso.NOME_NAO_INFORMADO);
        }

        if(jaExisteByNome(grupoAcessoDto.getNome())){
            throw new ValidacaoException(TipoErroGrupoAcesso.NOME_JA_EXISTE);
        }

        if(grupoAcessoDto.getRoles() == null || grupoAcessoDto.getRoles().isEmpty()){
            throw new ValidacaoException(TipoErroGrupoAcesso.NENHUMA_ROLE_INFORMADA);
        }

    }

    private boolean jaExisteByNome(@NonNull final String nome){
        return buscarByNome(nome) != null;
    }

    private GrupoAcesso buscarByNome(@NonNull final String nome){
        return grupoAcessoRepository.findByNome(nome);
    }

    public List<GrupoAcessoDto> listarGrupoAcesso(){
        return grupoAcessoRepository.findAll().stream()
                .map(grupoAcessoMapper::toDto).toList();
    }

    public List<UsuarioDto> listarTodos(){
        return usuarioMapper.toDto(usuarioRepository.findAll());
    }

}
