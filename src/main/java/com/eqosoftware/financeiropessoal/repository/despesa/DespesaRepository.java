package com.eqosoftware.financeiropessoal.repository.despesa;

import com.eqosoftware.financeiropessoal.domain.despesa.Despesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    Despesa findDespesaByUuid(UUID uuid);

    Page<Despesa> findAllByDescricaoIgnoreCaseContains(@Param("descricao") String descricao, Pageable page);

}
