package com.eqosoftware.financeiropessoal.repository.despesa;

import com.eqosoftware.financeiropessoal.domain.despesa.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {
}
