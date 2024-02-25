package com.eqosoftware.financeiropessoal.repository.recorrente;

import com.eqosoftware.financeiropessoal.domain.recorrente.Recorrente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecorrenteRepository extends JpaRepository<Recorrente, Long> {
}
