package com.eqosoftware.financeiropessoal.config.security;

import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import com.eqosoftware.financeiropessoal.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import/**
 * Created by erik on 28/01/2022.
 */ org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by erik on 28/01/2022.
 */

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService usuariosService;

    @Override @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuariosService.buscarByEmailOrUsername(username);
        if(usuario != null){
            return new UsuarioSistema(usuario, getGrupos(usuario));
        }else{
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
    }

    private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {
        return usuario.getGrupoAcesso().getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.toUpperCase()))
                .collect(Collectors.toList());
    }

}
