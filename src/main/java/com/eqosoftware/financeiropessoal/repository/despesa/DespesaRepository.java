package com.eqosoftware.financeiropessoal.repository.despesa;

import com.eqosoftware.financeiropessoal.domain.despesa.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long>, JpaSpecificationExecutor<Despesa> {

    Optional<Despesa> findDespesaByUuid(UUID uuid);
    List<Despesa> findDespesaByUuidIn(List<UUID> uuid);

    List<Despesa> findAllByMesCompetencia(LocalDate competencia);

    List<Despesa> findAllByDespesaPai_Id(Long idDespesaPai);

    List<Despesa> findAllByMetodoPagamento_Uuid(UUID uuid);

}
