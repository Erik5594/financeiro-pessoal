package com.eqosoftware.financeiropessoal.repository.perfil;

import com.eqosoftware.financeiropessoal.domain.perfil.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByUsuario_Username(String username);

}
