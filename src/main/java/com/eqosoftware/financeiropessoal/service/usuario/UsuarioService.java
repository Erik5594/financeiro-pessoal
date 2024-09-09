package com.eqosoftware.financeiropessoal.service.usuario;

import com.eqosoftware.financeiropessoal.config.security.UsuarioSistema;
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
import com.eqosoftware.financeiropessoal.util.IgnoresPropertiesUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
    @Autowired @Lazy
    private AuthenticationManager authenticationManager;

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

    @Transactional
    public UsuarioDto atualizar(UsuarioDto usuarioDto){
        var usuarioSistema =(UsuarioSistema) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var novosDados = usuarioMapper.toEntity(usuarioDto);
        var usuario = usuarioRepository.findUsuarioByEmailOrUsername(null, usuarioSistema.getUsuario().getUsername());
        BeanUtils.copyProperties(novosDados, usuario,
                IgnoresPropertiesUtils.ignorePropertiesAllBase("username", "senha", "ativo", "bloqueado", "grupoAcesso", "tenant"));
        var usuarioAtualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuarioAtualizado);
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

    public UsuarioDto atualizarSenha(String atual, String nova){
        var usuarioSistema = (UsuarioSistema) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validarSenhaAtual(atual, usuarioSistema);
        var usuario = usuarioMapper.toDto(atualizarSenhaBanco(nova, usuarioSistema.getUsername()));
        usuario.setSenha(nova);
        return usuario;
    }

    public void resetarSenha(String username){
        atualizarSenhaBanco("1234", username);
    }

    private Usuario atualizarSenhaBanco(String nova, String username) {
        var usuario = this.buscarByEmailOrUsername(username);
        if(Objects.isNull(usuario)){
            throw new ValidacaoException(TipoErroUsuario.USUARIO_NAO_ENCONTRADO);
        }
        usuario.setSenha(passwordEncoder.encode(nova));
        return usuarioRepository.save(usuario);
    }

    private void validarSenhaAtual(String atual, UsuarioSistema usuarioSistema) {
        try{
            authenticate(usuarioSistema.getUsername(), atual);
        } catch (Exception e){
            log.error("Erro ao validar senha atual", e);
            if(e instanceof BadCredentialsException) {
                throw new ValidacaoException("Senha atual não confere!", 400);
            }else{
                throw new ValidacaoException("Ocorreu um erro ao validar senha atual!", 500);
            }
        }
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }
    }

}
