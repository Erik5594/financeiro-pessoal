package com.eqosoftware.financeiropessoal.repository.usuario;

import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by erik on 28/01/2022.
 */

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findUsuarioByEmailOrUsername(String email, String username);
    Optional<Usuario> findByUuid(UUID uuid);

}
