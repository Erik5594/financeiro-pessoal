package com.eqosoftware.financeiropessoal.service.categoria;

import com.eqosoftware.financeiropessoal.domain.categoria.Categoria;
import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaDto;
import com.eqosoftware.financeiropessoal.repository.categoria.CategoriaRepository;
import com.eqosoftware.financeiropessoal.service.categoria.mapper.CategoriaMapper;
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
        Categoria categoria = categoriaMapper.toEntity(categoriaDto);
        if(Objects.nonNull(categoria.getCategoriaPai())){
            categoria.setCategoriaPai(buscar(categoria.getCategoriaPai().getUuid()));
        }
        categoria = categoriaRepository.save(categoria);
        CategoriaDto categoriaDto1 =  categoriaMapper.toDto(categoria);
        return;
    }

    private Categoria buscar(UUID categoriaId){
        return categoriaRepository.findCategoriaByUuid(categoriaId);
    }

    public List<CategoriaDto> listar(){
        return categoriaMapper.toDto(categoriaRepository.findAll());
    }

}
