package com.eqosoftware.financeiropessoal.repository.categoria;

import com.eqosoftware.financeiropessoal.domain.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Categoria findCategoriaByUuid(UUID uuid);

}
