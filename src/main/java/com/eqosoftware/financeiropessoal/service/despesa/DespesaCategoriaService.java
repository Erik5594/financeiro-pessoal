package com.eqosoftware.financeiropessoal.service.despesa;

import com.eqosoftware.financeiropessoal.domain.categoria.Categoria;
import com.eqosoftware.financeiropessoal.domain.despesa.Despesa;
import com.eqosoftware.financeiropessoal.domain.despesa.DespesaCategoria;
import com.eqosoftware.financeiropessoal.dto.despesa.DespesaCategoriaDto;
import com.eqosoftware.financeiropessoal.repository.categoria.CategoriaRepository;
import com.eqosoftware.financeiropessoal.repository.despesa.DespesaRepository;
import com.eqosoftware.financeiropessoal.repository.despesacategoria.DespesaCategoriaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class DespesaCategoriaService {

    private final DespesaCategoriaRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final DespesaRepository despesaRepository;

    @Autowired
    public DespesaCategoriaService(DespesaCategoriaRepository repository, CategoriaRepository categoriaRepository, DespesaRepository despesaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.despesaRepository = despesaRepository;
    }


    @Transactional
    public void atualizarDespesasCategoria(UUID uuidDespesa, List<DespesaCategoria> novosDadosDespesaCategoria){
        var despesaCategoriaVinculadas = repository.findAllByDespesa_Uuid(uuidDespesa);
        var uuidCategoriaNova = novosDadosDespesaCategoria.stream().map(DespesaCategoria::getCategoria).map(Categoria::getUuid).toList();
        var uuidCategoriaVinculadas = despesaCategoriaVinculadas.stream().map(DespesaCategoria::getCategoria).map(Categoria::getUuid).toList();

        var despesaCategoriaAtualizar = despesaCategoriaVinculadas.stream().filter(despesaCategoria -> uuidCategoriaNova.contains(despesaCategoria.getCategoria().getUuid())).toList();
        var despesaCategoriaRequestAtualizar = novosDadosDespesaCategoria.stream().filter(despesaCategoria -> uuidCategoriaVinculadas.contains(despesaCategoria.getCategoria().getUuid())).toList();
        var despesaCategoriaRemover = despesaCategoriaVinculadas.stream().filter(despesaCategoria -> !uuidCategoriaNova.contains(despesaCategoria.getCategoria().getUuid())).toList();
        var despesaCategoriaRequestAdicionar = novosDadosDespesaCategoria.stream().filter(despesaCategoria -> !uuidCategoriaVinculadas.contains(despesaCategoria.getCategoria().getUuid())).toList();

        despesaCategoriaRequestAtualizar.forEach(novosDados -> atualizar(novosDados, despesaCategoriaAtualizar));
        despesaCategoriaRemover.forEach(this::remover);
        despesaCategoriaRequestAdicionar.forEach(despesaCategoria -> adicionar(uuidDespesa, despesaCategoria));
    }

    public void remover(DespesaCategoria despesaCategoria){
        despesaCategoria.setDeleted(Instant.now());
        repository.saveAndFlush(despesaCategoria);
    }

    private void atualizar(DespesaCategoria novosDados, List<DespesaCategoria> vinculadas){
        vinculadas.stream()
                .filter(despesaCategoria -> despesaCategoria.getCategoria().getUuid().equals(novosDados.getCategoria().getUuid()))
                .findFirst()
                .ifPresent(atual -> atualizar(novosDados, atual));
    }

    private void atualizar(DespesaCategoria novosDados, DespesaCategoria atual){
        BeanUtils.copyProperties(novosDados, atual, "despesa", "categoria", "deleted", "createdBy", "createdDate", "id", "version", "uuid");
        repository.saveAndFlush(atual);
    }

    private void adicionar(UUID uuidDespesa, DespesaCategoria despesaCategoria){
        criarVinculoDespesaCategoria(uuidDespesa, despesaCategoria);
        repository.saveAndFlush(despesaCategoria);
    }

    private void criarVinculoDespesaCategoria(UUID uuidDespesa, DespesaCategoria despesaCategoria){
        var despesa = despesaRepository.findDespesaByUuid(uuidDespesa);
        despesaCategoria.setDespesa(despesa.orElseThrow());
        var categoria = categoriaRepository.findCategoriaByUuid(despesaCategoria.getCategoria().getUuid());
        if(Objects.nonNull(categoria))
            despesaCategoria.setCategoria(categoria);
    }

}
