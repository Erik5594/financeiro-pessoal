package com.eqosoftware.financeiropessoal.service.perfil;

import com.eqosoftware.financeiropessoal.domain.perfil.Perfil;
import com.eqosoftware.financeiropessoal.dto.token.UsuarioDto;
import com.eqosoftware.financeiropessoal.repository.perfil.PerfilRepository;
import com.eqosoftware.financeiropessoal.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PerfilService {

    private final UsuarioService usuarioService;
    private final PerfilRepository repository;

    @Autowired
    public PerfilService(UsuarioService usuarioService, PerfilRepository repository) {
        this.usuarioService = usuarioService;
        this.repository = repository;
    }

    public void criarParaNovoUsuario(UsuarioDto usuarioDto) {
        var usuario = usuarioService.buscarByUUID(usuarioDto.getId());
        var perfil = new Perfil();
        perfil.setNome(usuario.getNome());
        //perfil.setUsuario(usuario);
        perfil.setDataNascimento(LocalDate.now());
        perfil.setDescricao("");
        //repository.save(perfil);
    }
}
