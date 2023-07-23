package com.eqosoftware.financeiropessoal.service.categoria;

import com.eqosoftware.financeiropessoal.domain.categoria.Categoria;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroCategoria;
import com.eqosoftware.financeiropessoal.dto.categoria.*;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
import com.eqosoftware.financeiropessoal.repository.categoria.CategoriaRepository;
import com.eqosoftware.financeiropessoal.service.categoria.mapper.CategoriaMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by erik on 13/04/2022.
 */

@Service
public class CategoriaService {

    @Autowired
    private CategoriaMapper categoriaMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EntityManager entityManager;

    public void criar(CategoriaDto categoriaDto){
        validarNovaCategoria(categoriaDto);
        Categoria categoria = categoriaMapper.toEntity(categoriaDto);
        if(Objects.nonNull(categoria.getCategoriaPai())){
            categoria.setCategoriaPai(buscarById(categoria.getCategoriaPai().getUuid()));
        }
        categoriaRepository.save(categoria);
    }

    public void atualizar(UUID idCategoria, CategoriaDto categoriaDto){
        var categoriaBanco = categoriaRepository.findCategoriaByUuid(idCategoria);
        if(Objects.isNull(categoriaBanco)){
            throw new ValidacaoException(TipoErroCategoria.NAO_ENCONTRADA);
        }
        validarNovaCategoria(categoriaDto);
        categoriaBanco.setNome(categoriaDto.getNome());
        categoriaBanco.setDescricao(categoriaDto.getDescricao());
        categoriaRepository.save(categoriaBanco);
    }

    private void validarNovaCategoria(CategoriaDto categoriaDto){
        if(StringUtils.isBlank(categoriaDto.getNome())){
            throw new ValidacaoException(TipoErroCategoria.NOME_NAO_INFORMADO);
        }

        /*if(StringUtils.isBlank(categoriaDto.getDescricao())){
            throw new ValidacaoException(TipoErroCategoria.DESCRICAO_NAO_INFORMADO);
        }*/

        if(Objects.isNull(categoriaDto.getNatureza())){
            throw new ValidacaoException(TipoErroCategoria.NATUREZA_NAO_INFORMADO);
        }

        if(jaExiste(categoriaDto.getNome(), categoriaDto.getIdCategoriaPai())){
            throw new ValidacaoException(TipoErroCategoria.JA_EXISTE);
        }
    }

    private boolean jaExiste(@NonNull final String nome, UUID idCategoriaPai){
        Categoria categoria = categoriaRepository.findByNome(nome);

        if(Objects.isNull(categoria)){
            return false;
        }
        if(Objects.isNull(idCategoriaPai) && Objects.nonNull(categoria.getCategoriaPai())){
            return false;
        }

        if(Objects.nonNull(idCategoriaPai) && Objects.isNull(categoria.getCategoriaPai())){
            return false;
        }

        if(Objects.nonNull(idCategoriaPai) && !idCategoriaPai.equals(categoria.getCategoriaPai().getUuid())){
            return false;
        }

        return true;
    }

    private Categoria buscarById(UUID categoriaId){
        return categoriaRepository.findCategoriaByUuid(categoriaId);
    }

    public List<CategoriaDto> listar(FiltroDto filtroDto){
        if(filtroDto.semFiltro()){
            return categoriaRepository.findAll().stream().map(categoriaMapper::toDto).toList();
        }else{
            return this.buscarComFiltro(filtroDto);
        }
    }

    public CategoriaDto acrescentarNomeCategoriaPai(CategoriaDto categoriaDto){
        Categoria categoriaPai = categoriaRepository.findCategoriaByUuid(categoriaDto.getIdCategoriaPai());
        categoriaDto.setNome(categoriaPai.getNome() +" >> "+ categoriaDto.getNome());
        return categoriaDto;
    }

    private List<CategoriaDto> buscarComFiltro(FiltroDto filtro){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Categoria.class);

        var predicate = criteriaBuilder.conjunction();
        var root = query.from(Categoria.class);

        if(!filtro.semFiltro()){
            if(StringUtils.isNotBlank(filtro.nome())){
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("nome"), "%"+filtro.nome()+"%"));
            }
            if(Objects.nonNull(filtro.natureza())){
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("natureza"), filtro.natureza()));
            }
        }

        query.where(predicate);

        var categorias = entityManager.createQuery(query)
                .getResultStream();

        if(!filtro.semFiltro() && Boolean.TRUE.equals(filtro.ultimaFilha())){
            return categorias.filter(categoria -> CollectionUtils.isEmpty(categoria.getCategoriasFilha()))
                    .map(categoriaMapper::toDto).toList();
        }
        return categorias.map(categoriaMapper::toDto).toList();
    }

    public CategoriaNaturezaTreeDto buscarTodasToTree(){
        List<Categoria> categorias = categoriaRepository.findAllByCategoriaPaiIsNull();
        List<Categoria> despesas = categorias
                .stream()
                .filter(categoria -> TipoNatureza.DESPESA.equals(categoria.getNatureza()))
                .toList();
        List<Categoria> receitas = categorias
                .stream()
                .filter(categoria -> TipoNatureza.RECEITA.equals(categoria.getNatureza()))
                .toList();
        List<CategoriaTreeDto> despesasTree = despesas.stream().map(this.categoriaMapper::toTree).toList();
        List<CategoriaTreeDto> receitasTree = receitas.stream().map(this.categoriaMapper::toTree).toList();
        return new CategoriaNaturezaTreeDto(receitasTree, despesasTree);
    }

    public CategoriaDto buscar(UUID categoriaId){
        Categoria categoria = buscarById(categoriaId);
        if(Objects.isNull(categoria)){
            throw new ValidacaoException(TipoErroCategoria.NAO_ENCONTRADA);
        }
        return categoriaMapper.toDto(categoria);
    }

    public void deletar(UUID idCategoria){
        Categoria categoria = categoriaRepository.findCategoriaByUuid(idCategoria);
        validarExclusao(categoria);
        categoriaRepository.delete(categoria);
    }

    private void validarExclusao(Categoria categoria){
        if(Objects.isNull(categoria)){
            throw new ValidacaoException(TipoErroCategoria.NAO_ENCONTRADA);
        }

        if(categoria.getCategoriasFilha() != null && !categoria.getCategoriasFilha().isEmpty()){
            throw new ValidacaoException(TipoErroCategoria.NAO_PODE_EXCLUIR_COM_FILHAS);
        }
    }

}
