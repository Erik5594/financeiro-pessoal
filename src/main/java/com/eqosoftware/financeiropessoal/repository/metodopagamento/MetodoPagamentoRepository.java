package com.eqosoftware.financeiropessoal.repository.metodopagamento;

import com.eqosoftware.financeiropessoal.domain.metodopagamento.MetodoPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MetodoPagamentoRepository  extends JpaRepository<MetodoPagamento, Long>{

    @Query(value = "select * from metodo_pagamento where upper(nome) = upper(:nome);", nativeQuery = true)
    MetodoPagamento findByNome(@Param("nome") String nome);
    Page<MetodoPagamento> findAllByNomeIgnoreCaseContains(@Param("nome") String nome, Pageable page);
    Optional<MetodoPagamento> findByUuid(@Param("uuid") UUID uuid);
    Optional<MetodoPagamento> findByPadraoIsTrue();

}
