package com.eqosoftware.financeiropessoal.service.despesa;

import com.eqosoftware.financeiropessoal.domain.categoria.Categoria;
import com.eqosoftware.financeiropessoal.domain.despesa.Despesa;
import com.eqosoftware.financeiropessoal.domain.despesa.DespesaCategoria;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroDespesa;
import com.eqosoftware.financeiropessoal.domain.recorrente.Recorrente;
import com.eqosoftware.financeiropessoal.dto.dashboard.TotalizadorDespesaPorSituacao;
import com.eqosoftware.financeiropessoal.dto.despesa.DespesaDto;
import com.eqosoftware.financeiropessoal.dto.despesa.FiltroDespesaDto;
import com.eqosoftware.financeiropessoal.dto.despesa.TipoSituacao;
import com.eqosoftware.financeiropessoal.dto.recorrente.PreviaRecorrenciaDto;
import com.eqosoftware.financeiropessoal.dto.recorrente.RecorrenteDto;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
import com.eqosoftware.financeiropessoal.repository.categoria.CategoriaRepository;
import com.eqosoftware.financeiropessoal.repository.despesa.DespesaRepository;
import com.eqosoftware.financeiropessoal.repository.metodopagamento.MetodoPagamentoRepository;
import com.eqosoftware.financeiropessoal.service.categoria.CategoriaService;
import com.eqosoftware.financeiropessoal.service.despesa.mapper.DespesaMapper;
import com.eqosoftware.financeiropessoal.service.recorrente.RecorrenteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class DespesaService {

    private final DespesaCategoriaService despesaCategoriaService;
    private final DespesaRepository repository;
    private final DespesaMapper despesaMapper;
    private final MetodoPagamentoRepository metodoPagamentoRepository;
    private final CategoriaRepository categoriaRepository;
    private final DespesaFiltroToSpecification specification;
    private final CategoriaService categoriaService;
    private final RecorrenteService recorrenteService;

    @Autowired
    public DespesaService(DespesaCategoriaService despesaCategoriaService, DespesaRepository despesaRepository, DespesaMapper despesaMapper,
                          MetodoPagamentoRepository metodoPagamentoRepository, CategoriaRepository categoriaRepository,
                          DespesaFiltroToSpecification specification, CategoriaService categoriaService,
                          RecorrenteService recorrenteService) {
        this.despesaCategoriaService = despesaCategoriaService;
        this.repository = despesaRepository;
        this.despesaMapper = despesaMapper;
        this.metodoPagamentoRepository = metodoPagamentoRepository;
        this.categoriaRepository = categoriaRepository;
        this.specification = specification;
        this.categoriaService = categoriaService;
        this.recorrenteService = recorrenteService;
    }

    @Transactional
    public void criar(DespesaDto despesaDto){
        validarNovaDespesa(despesaDto);
        if(Objects.nonNull(despesaDto.getRecorrencia())){
            criarDespesasRecorrentes(despesaDto);
        }else{
            var despesa = despesaMapper.toEntity(despesaDto);
            despesa.setMesCompetencia(despesa.getMesCompetencia().withDayOfMonth(1));
            criarVinculos(despesa);
            if(isParcelada(despesaDto.getQtdeParcela())){
                var despesaPai = repository.saveAndFlush(despesa);
                criarParcelas(despesaPai);
            }else{
                repository.save(despesa);
            }
        }
    }

    private void criarDespesasRecorrentes(DespesaDto despesaDto){
        var recorrencia = despesaDto.getRecorrencia();
        validarCamposDaRecorrencia(recorrencia);
        var recorrente = recorrenteService.criar(recorrencia);
        criarDespesasRecorrentes(recorrencia.getDatasRecorrencias(), despesaDto, recorrente);
    }

    private void validarCamposDaRecorrencia(RecorrenteDto recorrenteDto){
        if(Objects.isNull(recorrenteDto.getFrequencia())){
            throw new ValidacaoException(TipoErroDespesa.FREQUENCIA_NAO_INFORMADA);
        }

        if(Objects.isNull(recorrenteDto.getDataLimite())){
            throw new ValidacaoException(TipoErroDespesa.DATA_LIMITE_RECORRENCIA);
        }

        if(CollectionUtils.isEmpty(recorrenteDto.getDatasRecorrencias()) || recorrenteDto.getDatasRecorrencias().size() < 2){
            throw new ValidacaoException(TipoErroDespesa.QTDE_RECORRENCIA_MENOR);
        }
    }

    private void criarDespesasRecorrentes(List<PreviaRecorrenciaDto> datasRecorrentes, DespesaDto despesa, Recorrente recorrente){
        datasRecorrentes.forEach(dataRecorrencia -> this.salvarDespesaByRecorrencia(dataRecorrencia, despesa, recorrente));
    }

    private void salvarDespesaByRecorrencia(PreviaRecorrenciaDto dataRecorrencia, DespesaDto despesaDto, Recorrente recorrente){
        var despesa = despesaMapper.toEntity(despesaDto);
        criarVinculos(despesa);
        despesa.setMesCompetencia(dataRecorrencia.competencia());
        despesa.setDataLancamento(dataRecorrencia.lancamento());
        despesa.setDataVencimento(dataRecorrencia.vencimento());
        despesa.setRecorrencia(recorrente);
        repository.save(despesa);
    }

    private void criarParcelas(Despesa despesaPai){
        var parcela = despesaPai.getNumParcela()+1;
        var mesCompetencia = despesaPai.getMesCompetencia().plusMonths(1);
        var vencimento = despesaPai.getDataVencimento().plusMonths(1);
        for(;parcela <= despesaPai.getQtdeParcela();parcela++){
            var despesa = new Despesa();
            BeanUtils.copyProperties(despesaPai, despesa, "id", "uuid", "version", "mesCompetencia", "despesaPai", "categorias");
            despesa.setDespesaPai(despesaPai);
            despesa.setMesCompetencia(mesCompetencia);
            despesa.setDataVencimento(vencimento);
            despesa.setNumParcela(parcela);
            despesa.setCategorias(despesaPai.getCategorias().stream().map(this::copiarDespesaCategoria).toList());
            if(!vencimento.isBefore(LocalDate.now())){
                despesa.setSituacao(TipoSituacao.EM_ABERTO);
            }
            criarVinculos(despesa);
            repository.save(despesa);
            mesCompetencia = mesCompetencia.plusMonths(1);
            vencimento = vencimento.plusMonths(1);
        }
    }

    private DespesaCategoria copiarDespesaCategoria(DespesaCategoria despesaCategoria){
        var despesaCategoriaNew = new DespesaCategoria();
        BeanUtils.copyProperties(despesaCategoria, despesaCategoriaNew, "categoria", "id", "uuid");
        despesaCategoriaNew.setCategoria(copiarCategoria(despesaCategoria.getCategoria()));
        return despesaCategoriaNew;
    }

    private Categoria copiarCategoria(Categoria categoria){
        var categoriaNew = new Categoria();
        BeanUtils.copyProperties(categoria, categoriaNew);
        return categoriaNew;
    }

    private boolean isParcelada(int qtdeParcela){
        return qtdeParcela > 0;
    }

    public Page<DespesaDto> listar(FiltroDespesaDto filtro, Pageable pageable){
        var despesas = repository.findAll(specification.toSpecification(filtro), pageable);
        var despesasMap = despesas.map(despesaMapper::toDto).map(this::atualizarSituacaoVencido);
        despesasMap.forEach(this::atualizarNomeCategoria);
        return despesasMap;
    }

    private void atualizarNomeCategoria(DespesaDto despesaDto){
        despesaDto.getCategorias()
                .forEach(categoriaDespesa -> categoriaService.acrescentarNomeCategoriaPai(categoriaDespesa.getCategoria()));
    }

    private DespesaDto atualizarSituacaoVencido(DespesaDto despesa){
        var despesaNew = new DespesaDto();
        BeanUtils.copyProperties(despesa, despesaNew);
        if(despesa.getDataVencimento().isBefore(LocalDate.now())
                && TipoSituacao.EM_ABERTO.equals(despesa.getSituacao())){
            despesaNew.setSituacao(TipoSituacao.VENCIDA);
        }
        return despesaNew;
    }

    public DespesaDto buscar(UUID categoriaId){
        var despesa = buscarById(categoriaId);
        if(Objects.isNull(despesa)){
            throw new ValidacaoException(TipoErroDespesa.NAO_ENCONTRADA);
        }
        return despesaMapper.toDto(despesa);
    }

    private Despesa buscarById(UUID despesaId){
        return repository.findDespesaByUuid(despesaId).orElseThrow();
    }

    public void atualizar(UUID idDespesa, DespesaDto despesaDto){
        var despesaBancoOpt = repository.findDespesaByUuid(idDespesa);
        if(despesaBancoOpt.isEmpty()){
            throw new ValidacaoException(TipoErroDespesa.NAO_ENCONTRADA);
        }
        var despesaBanco = despesaBancoOpt.get();
        validarNovaDespesa(despesaDto);
        var novosDados = despesaMapper.toEntity(despesaDto);
        novosDados.setMesCompetencia(novosDados.getMesCompetencia().withDayOfMonth(1));
        if(isParcelada(despesaDto.getQtdeParcela())){
            despesaBanco.setSituacao(novosDados.getSituacao());
            despesaBanco.setObservacao(novosDados.getObservacao());
            repository.save(despesaBanco);
        }else{
            BeanUtils.copyProperties(novosDados, despesaBanco, "categorias","deleted", "createdBy", "createdDate", "id", "version", "uuid");
            criarVinculoMetodoPagamento(despesaBanco);
            repository.saveAndFlush(despesaBanco);
            despesaCategoriaService.atualizarDespesasCategoria(idDespesa, novosDados.getCategorias());
        }
    }

    @Transactional
    public void pagar(UUID idDespesa){
        var despesaBancoOpt = repository.findDespesaByUuid(idDespesa);
        despesaBancoOpt.ifPresent(this::pagarDespesa);
    }

    @Transactional
    public void pagarVarias(List<UUID> idsDespesa){
        var despesaBancoList = repository.findDespesaByUuidIn(idsDespesa);
        despesaBancoList.forEach(this::pagarDespesa);
    }

    private void pagarDespesa(Despesa despesa){
        despesa.setSituacao(TipoSituacao.PAGO);
        repository.save(despesa);
    }

    private void criarVinculos(Despesa despesa) {
        criarVinculoCategoriaDespesa(despesa);
        criarVinculoMetodoPagamento(despesa);
    }

    private void criarVinculoCategoriaDespesa(Despesa despesa) {
        despesa.getCategorias()
                .stream()
                .filter(despesaCategoria -> Objects.isNull(despesaCategoria.getDeleted()))
                .forEach(despesaCategoria -> this.criarVinculoDespesaCategoria(despesa, despesaCategoria));
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
            throw new ValidacaoException(TipoErroDespesa.DESCRICAO_NAO_INFORMADO);
        }

        if(CollectionUtils.isEmpty(despesaDto.getCategorias())){
            throw new ValidacaoException(TipoErroDespesa.CATEGORIA_NAO_INFORMADO);
        }

        if(isParcelada(despesaDto.getQtdeParcela())){
            if(despesaDto.getNumParcela() > despesaDto.getQtdeParcela()){
                throw new ValidacaoException(TipoErroDespesa.NUM_PARCELA_MAIOR_QUE_QTDE_PARCELA);
            }
        }

    }

    @Transactional
    public void deletar(UUID idDespesa){
        var despesa = repository.findDespesaByUuid(idDespesa);
        despesa.ifPresent(this::deletarDespesa);
    }

    @Transactional
    public void deletarVarios(List<UUID> idsDespesa){
        var despesaList = repository.findDespesaByUuidIn(idsDespesa);
        var despesasParaExcluir = despesaList.stream().filter(despesa -> despesa.getQtdeParcela() == 0);
        despesasParaExcluir.forEach(this::deletarDespesa);
    }

    private void deletarDespesa(Despesa despesa){
        if(isParcelada(despesa.getQtdeParcela())){
            removerDespesaParcelada(despesa);
        }else{
            removerDespesaSimples(despesa);
        }
    }

    private void removerDespesaSimples(Despesa despesa){
        despesa.getCategorias().forEach(despesaCategoriaService::remover);
        validarExclusao(despesa);
        despesa.setDeleted(Instant.now());
        repository.saveAndFlush(despesa);
    }

    private void removerDespesaParcelada(Despesa despesa){
        var isParcelaPai = Objects.isNull(despesa.getDespesaPai());
        var idDespesaPai = isParcelaPai ? despesa.getId() : despesa.getDespesaPai().getId();
        var parcelasFilhas = repository.findAllByDespesaPai_Id(idDespesaPai);
        parcelasFilhas.forEach(this::removerDespesaSimples);
        if(isParcelaPai){
            removerDespesaSimples(despesa);
        }else{
            var despesaPai = repository.findById(idDespesaPai);
            despesaPai.ifPresent(this::removerDespesaSimples);
        }
    }

    private void validarExclusao(Despesa despesa){
        if(Objects.isNull(despesa)){
            throw new ValidacaoException(TipoErroDespesa.NAO_ENCONTRADA);
        }
    }

    public TotalizadorDespesaPorSituacao buscarTotalizadorPorCompetencia(LocalDate competencia){
        if(Objects.isNull(competencia)){
            competencia = LocalDate.now().withDayOfMonth(1);
        }else{
            competencia = competencia.withDayOfMonth(1);
        }

        var despesas = repository.findAllByMesCompetencia(competencia);

        var despesasPagas = despesas.stream()
                .filter(despesa -> TipoSituacao.PAGO == despesa.getSituacao()).toList();

        var despesasEmAberto = despesas.stream()
                .filter(despesa -> TipoSituacao.EM_ABERTO == despesa.getSituacao()
                        && !despesa.getDataVencimento().isBefore(LocalDate.now())).toList();

        var despesasVencidas = despesas.stream()
                .filter(despesa -> TipoSituacao.EM_ABERTO == despesa.getSituacao()
                        && despesa.getDataVencimento().isBefore(LocalDate.now())).toList();

        var qtdeDespesasPagas = despesasPagas.size();
        var qtdeDespesasEmAberto = despesasEmAberto.size();
        var qtdeDespesasVencidas = despesasVencidas.size();

        var totalDespesasPagas = despesasPagas.stream()
                .map(Despesa::getCategorias).flatMap(List::stream)
                .map(DespesaCategoria::getValor).mapToDouble(BigDecimal::doubleValue).sum();

        var totalDespesasEmAberto = despesasEmAberto.stream()
                .flatMap(despesa -> despesa.getCategorias().stream())
                .map(DespesaCategoria::getValor).mapToDouble(BigDecimal::doubleValue).sum();

        var totalDespesasVencidas = despesasVencidas.stream()
                .flatMap(despesa -> despesa.getCategorias().stream())
                .map(DespesaCategoria::getValor).mapToDouble(BigDecimal::doubleValue).sum();

        return new TotalizadorDespesaPorSituacao(
                totalDespesasPagas,totalDespesasEmAberto,totalDespesasVencidas,
                qtdeDespesasPagas, qtdeDespesasEmAberto, qtdeDespesasVencidas
        );
    }

}
