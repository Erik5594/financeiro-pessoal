package com.eqosoftware.financeiropessoal.repository.categoria;

import com.eqosoftware.financeiropessoal.domain.categoria.Categoria;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@SQLDelete(sql = "DELETE FROM categoria where id = ?")
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Categoria findCategoriaByUuid(UUID uuid);

    @Query(value = "select * from categoria where upper(nome) = upper(:nome);", nativeQuery = true)
    Categoria findByNome(@Param("nome") String nome);

    List<Categoria> findAllByCategoriaPaiIsNull();

}
