package com.eqosoftware.financeiropessoal.repository.usuario;

import com.eqosoftware.financeiropessoal.domain.auth.GrupoAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by erik on 28/01/2022.
 */

@Repository
public interface GrupoAcessoRepository extends JpaRepository<GrupoAcesso, Long> {

    @Query(value = "select * from grupo_acesso where upper(nome) = upper(:nome);", nativeQuery = true)
    GrupoAcesso findByNome(@Param("nome") String nome);

}
