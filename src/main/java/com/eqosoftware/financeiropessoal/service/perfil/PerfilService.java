package com.eqosoftware.financeiropessoal.service.perfil;

import com.eqosoftware.financeiropessoal.config.security.UsuarioSistema;
import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import com.eqosoftware.financeiropessoal.domain.perfil.Perfil;
import com.eqosoftware.financeiropessoal.dto.perfil.PerfilDto;
import com.eqosoftware.financeiropessoal.dto.perfil.PerfilRequestAtualizaDto;
import com.eqosoftware.financeiropessoal.dto.token.UsuarioDto;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
import com.eqosoftware.financeiropessoal.repository.perfil.PerfilRepository;
import com.eqosoftware.financeiropessoal.service.perfil.mapper.PerfilMapper;
import com.eqosoftware.financeiropessoal.service.usuario.UsuarioService;
import com.eqosoftware.financeiropessoal.util.FileUtils;
import com.eqosoftware.financeiropessoal.util.IgnoresPropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class PerfilService {

    private final UsuarioService usuarioService;
    private final PerfilRepository repository;
    private final PerfilMapper mapper;
    private final String BASE64_FORMAT = "data:image/%s;base64,%s";

    @Autowired
    public PerfilService(UsuarioService usuarioService, PerfilRepository repository, PerfilMapper mapper) {
        this.usuarioService = usuarioService;
        this.repository = repository;
        this.mapper = mapper;
    }

    public PerfilDto criarParaNovoUsuario() {
        var usuarioSistema = (UsuarioSistema) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var usuario = usuarioService.buscarByEmailOrUsername(usuarioSistema.getUsername());
        var perfil = new Perfil();
        perfil.setNome(usuario.getNome());
        perfil.setUsuario(usuario);
        perfil.setDataNascimento(LocalDate.now());
        return mapper.toDto(repository.saveAndFlush(perfil));
    }

    @Transactional
    public void atualizar(PerfilRequestAtualizaDto perfilRequestAtualizaDto){
        usuarioService.atualizar(new UsuarioDto(perfilRequestAtualizaDto.nome(), perfilRequestAtualizaDto.email()));
        var perfilOpt = buscar();
        if(perfilOpt.isPresent()){
            var perfilDto = mapper.toDtoAux(perfilRequestAtualizaDto);
            var perfil = perfilOpt.get();
            var novosDados = mapper.toEntity(perfilDto);
            BeanUtils.copyProperties(novosDados, perfil, IgnoresPropertiesUtils.ignorePropertiesAllBase("usuario", "urlImagemPerfil"));
            repository.save(perfil);
        }
    }

    public PerfilDto buscarOuCriarPerfil(){
        var perfilOpt = buscar();
        if(perfilOpt.isEmpty()){
            return criarParaNovoUsuario();
        }else{
            return converter(perfilOpt.get());
        }
    }

    public PerfilDto buscarPerfilDto(){
        var perfilOpt = buscar();
        if(perfilOpt.isPresent()){
            return converter(perfilOpt.get());
        }
        throw new ValidacaoException("Não foi encontrado esse perfil!", 404);
    }

    public Optional<Perfil> buscar(){
        var usuarioSistema = (UsuarioSistema) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        repository.flush();
        return repository.findByUsuario_Username(usuarioSistema.getUsername());
    }

    private PerfilDto converter(Perfil perfil){
        var perfilDto = mapper.toDto(perfil);
        return StringUtils.isNotBlank(perfilDto.urlImagemPerfil())
                ? ajustarImagem(perfilDto) : perfilDto;
    }

    private PerfilDto ajustarImagem(PerfilDto perfilDto) {
        var imagem = new File(perfilDto.urlImagemPerfil());
        var extensao = perfilDto.urlImagemPerfil().split("\\.")[1];
        try {
            var imagemBase64 = FileUtils.imageFileToBase64(imagem);
            return perfilDto.withUrlImagemPerfil(BASE64_FORMAT.formatted(extensao, imagemBase64));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void guardarImagem(String tenantId, MultipartFile imagem){
        try {
            var file = FileUtils.criarImagemPerfil(tenantId);
            if(Objects.nonNull(file)){
                FileUtils.escreverImagem(file.getAbsolutePath(), imagem.getBytes());
                atualizarImagemPerfilBanco(file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removerImagem(String tenantId){
        if(FileUtils.excluirImagemPerfil(tenantId)){
            atualizarImagemPerfilBanco(null);
        }
    }

    private void atualizarImagemPerfilBanco(String absolutePath) {
        var usuarioSistema =(UsuarioSistema) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        repository.flush();
        var perfilOpt = repository.findByUsuario_Username(usuarioSistema.getUsername());
        if(perfilOpt.isPresent()){
            var perfil = perfilOpt.get();
            perfil.setUrlImagemPerfil(absolutePath);
            repository.save(perfil);
        }
    }

}
