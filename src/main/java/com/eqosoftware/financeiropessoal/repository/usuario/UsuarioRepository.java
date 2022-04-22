package com.eqosoftware.financeiropessoal.repository.usuario;

import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by erik on 28/01/2022.
 */

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Usuario findUsuarioByEmailOrUsername(String email, String username);

}
