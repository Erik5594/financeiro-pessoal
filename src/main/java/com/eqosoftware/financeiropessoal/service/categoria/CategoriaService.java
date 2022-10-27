package com.eqosoftware.financeiropessoal.service.categoria;

import com.eqosoftware.financeiropessoal.domain.categoria.Categoria;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroCategoria;
import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaDto;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
import com.eqosoftware.financeiropessoal.repository.categoria.CategoriaRepository;
import com.eqosoftware.financeiropessoal.service.categoria.mapper.CategoriaMapper;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by erik on 13/04/2022.
 */

@Service
public class CategoriaService {

    @Autowired
    private CategoriaMapper categoriaMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public void criar(CategoriaDto categoriaDto){
        validarNovaCategoria(categoriaDto);
        Categoria categoria = categoriaMapper.toEntity(categoriaDto);
        if(Objects.nonNull(categoria.getCategoriaPai())){
            categoria.setCategoriaPai(buscarById(categoria.getCategoriaPai().getUuid()));
        }
        categoriaRepository.save(categoria);
    }

    private void validarNovaCategoria(CategoriaDto categoriaDto){
        if(StringUtils.isBlank(categoriaDto.getNome())){
            throw new ValidacaoException(TipoErroCategoria.NOME_NAO_INFORMADO);
        }

        if(StringUtils.isBlank(categoriaDto.getDescricao())){
            throw new ValidacaoException(TipoErroCategoria.DESCRICAO_NAO_INFORMADO);
        }

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

        if(Objects.nonNull(idCategoriaPai) && Objects.nonNull(categoria.getCategoriaPai()) && !idCategoriaPai.equals(categoria.getCategoriaPai().getUuid())){
            return false;
        }

        return true;
    }

    private Categoria buscarById(UUID categoriaId){
        return categoriaRepository.findCategoriaByUuid(categoriaId);
    }

    public List<CategoriaDto> listar(){
        return categoriaMapper.toDto(categoriaRepository.findAll());
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
