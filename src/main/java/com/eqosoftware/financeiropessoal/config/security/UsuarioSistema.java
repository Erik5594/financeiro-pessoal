package com.eqosoftware.financeiropessoal.config.security;

import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by erik on 28/01/2022.
 */

public class UsuarioSistema extends User {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;


    public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        super(StringUtils.isNotBlank(usuario.getUsername()) ? usuario.getUsername() : usuario.getEmail(),
                usuario.getSenha(), authorities);
        this.usuario = usuario;
    }

}
