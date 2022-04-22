package com.eqosoftware.financeiropessoal.repository.usuario;

import com.eqosoftware.financeiropessoal.domain.auth.GrupoAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by erik on 28/01/2022.
 */

@Repository
public interface GrupoAcessoRepository extends JpaRepository<GrupoAcesso, Long> {
}
