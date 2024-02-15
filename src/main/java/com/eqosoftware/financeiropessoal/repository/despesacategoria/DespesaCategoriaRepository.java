package com.eqosoftware.financeiropessoal.repository.despesacategoria;

import com.eqosoftware.financeiropessoal.domain.despesa.DespesaCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DespesaCategoriaRepository extends JpaRepository<DespesaCategoria, Long> {

    List<DespesaCategoria> findAllByDespesa_Uuid(UUID uuid);

}
