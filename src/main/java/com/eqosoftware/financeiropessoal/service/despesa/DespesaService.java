package com.eqosoftware.financeiropessoal.service.despesa;

import com.eqosoftware.financeiropessoal.domain.despesa.Despesa;
import com.eqosoftware.financeiropessoal.domain.despesa.DespesaCategoria;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroCategoria;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroDespesa;
import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaDto;
import com.eqosoftware.financeiropessoal.dto.despesa.DespesaDto;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
import com.eqosoftware.financeiropessoal.repository.categoria.CategoriaRepository;
import com.eqosoftware.financeiropessoal.repository.despesa.DespesaRepository;
import com.eqosoftware.financeiropessoal.repository.metodopagamento.MetodoPagamentoRepository;
import com.eqosoftware.financeiropessoal.service.despesa.mapper.DespesaMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private DespesaMapper despesaMapper;

    @Autowired
    private MetodoPagamentoRepository metodoPagamentoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public void criar(DespesaDto despesaDto){
        validarNovaDespesa(despesaDto);
        var despesa = despesaMapper.toEntity(despesaDto);
        criarVinculos(despesa);
        despesaRepository.save(despesa);
    }

    public Page<DespesaDto> listar(String descricao, Pageable pageable){
        var despesas = StringUtils.isBlank(descricao) ?
                despesaRepository.findAll(pageable) :
                despesaRepository.findAllByDescricaoIgnoreCaseContains(descricao, pageable);
        return despesas.map(despesaMapper::toDto);
    }

    public DespesaDto buscar(UUID categoriaId){
        var despesa = buscarById(categoriaId);
        if(Objects.isNull(despesa)){
            throw new ValidacaoException(TipoErroDespesa.NAO_ENCONTRADA);
        }
        return despesaMapper.toDto(despesa);
    }

    private Despesa buscarById(UUID despesaId){
        return despesaRepository.findDespesaByUuid(despesaId);
    }

    public void atualizar(UUID idDespesa, DespesaDto despesaDto){
        var despesaBanco = despesaRepository.findDespesaByUuid(idDespesa);
        if(Objects.isNull(despesaBanco)){
            throw new ValidacaoException(TipoErroDespesa.NAO_ENCONTRADA);
        }
        var novosDados = despesaMapper.toEntity(despesaDto);
        validarNovaDespesa(despesaDto);
        BeanUtils.copyProperties(novosDados, despesaBanco, "categorias", "deleted", "createdBy", "createdDate", "id", "version", "uuid");
        criarVinculos(despesaBanco);
        despesaRepository.save(despesaBanco);
    }

    private void criarVinculos(Despesa despesa) {
        criarVinculoCategoriaDespesa(despesa);
        criarVinculoMetodoPagamento(despesa);
    }

    private void criarVinculoCategoriaDespesa(Despesa despesa) {
        despesa.getCategorias().forEach(despesaCategoria -> this.criarVinculoDespesaCategoria(despesa, despesaCategoria));
    }

    private void criarVinculoDespesaCategoria(Despesa despesa, DespesaCategoria despesaCategoria){
        despesaCategoria.setDespesa(despesa);
        var categoria = categoriaRepository.findCategoriaByUuid(despesaCategoria.getCategoria().getUuid());
        if(Objects.nonNull(categoria))
            despesaCategoria.setCategoria(categoria);
    }

    private void criarVinculoMetodoPagamento(Despesa despesa){
        var metodoPagamentoOpt = metodoPagamentoRepository.findByUuid(despesa.getMetodoPagamento().getUuid());
        metodoPagamentoOpt.ifPresent(despesa::setMetodoPagamento);
    }

    private void validarNovaDespesa(DespesaDto despesaDto){
        if(StringUtils.isBlank(despesaDto.getDescricao())){
            throw new ValidacaoException(TipoErroCategoria.DESCRICAO_NAO_INFORMADO);
        }
    }

    public void deletar(UUID idDespesa){
        var despesa = despesaRepository.findDespesaByUuid(idDespesa);
        validarExclusao(despesa);
        despesaRepository.delete(despesa);
    }

    private void validarExclusao(Despesa despesa){
        if(Objects.isNull(despesa)){
            throw new ValidacaoException(TipoErroDespesa.NAO_ENCONTRADA);
        }
    }

}
