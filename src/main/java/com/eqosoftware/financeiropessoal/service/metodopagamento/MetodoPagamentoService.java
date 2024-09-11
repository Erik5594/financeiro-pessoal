package com.eqosoftware.financeiropessoal.service.metodopagamento;

import com.eqosoftware.financeiropessoal.domain.erro.TipoErroDespesa;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroMetodoPagamento;
import com.eqosoftware.financeiropessoal.domain.metodopagamento.MetodoPagamento;
import com.eqosoftware.financeiropessoal.domain.metodopagamento.TipoLancamentoCompetencia;
import com.eqosoftware.financeiropessoal.domain.metodopagamento.TipoMetodoPagamento;
import com.eqosoftware.financeiropessoal.dto.pagamento.DatasResponse;
import com.eqosoftware.financeiropessoal.dto.pagamento.MetodoPagamentoDto;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
import com.eqosoftware.financeiropessoal.repository.despesa.DespesaRepository;
import com.eqosoftware.financeiropessoal.repository.metodopagamento.MetodoPagamentoRepository;
import com.eqosoftware.financeiropessoal.service.metodopagamento.mapper.MetodoPagamentoMapper;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.UUID;

@Service
public class MetodoPagamentoService {

    private final MetodoPagamentoMapper mapper;
    private final MetodoPagamentoRepository repository;
    private final DespesaRepository despesaRepository;

    @Autowired
    public MetodoPagamentoService(MetodoPagamentoMapper mapper, MetodoPagamentoRepository repository, DespesaRepository despesaRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.despesaRepository = despesaRepository;
    }

    @Transactional
    public void criar(MetodoPagamentoDto metodoPagamentoDto){
        validarNovoMetodoPagamento(metodoPagamentoDto);
        if(Boolean.TRUE.equals(metodoPagamentoDto.padrao())){
            var metodoPagamentoOpt = repository.findByPadraoIsTrue();
            metodoPagamentoOpt.ifPresent(this::marcarComoNaoPadrao);
        }
        var metodoPagamento = mapper.toEntity(metodoPagamentoDto);
        metodoPagamento.setPadrao(Boolean.TRUE.equals(metodoPagamento.getPadrao()));
        corrigirTipoMetodoPagamento(metodoPagamento);
        corrigirTipoLancamentoCompetencia(metodoPagamento);
        repository.save(metodoPagamento);
    }

    private void marcarComoNaoPadrao(MetodoPagamento metodoPagamento){
        metodoPagamento.setPadrao(false);
        repository.save(metodoPagamento);
    }

    public void remover(UUID uuidMetodoPagamento){
        var metodoPagamentoOpt = repository.findByUuid(uuidMetodoPagamento);
        metodoPagamentoOpt.ifPresent(repository::delete);
    }

    public Page<MetodoPagamentoDto> listarMetodosPagamento(String nome, Pageable pageable){
        var metodosPagamento = StringUtils.isBlank(nome) ?
                repository.findAll(pageable) :
                repository.findAllByNomeIgnoreCaseContains(nome, pageable);
        metodosPagamento.forEach(this::corrigirTipoMetodoPagamento);
        metodosPagamento.forEach(this::corrigirTipoLancamentoCompetencia);
        var metodosPagamentoDto =  metodosPagamento.map(mapper::toDto);
        return metodosPagamentoDto.map(this::ajustaDespesaVinculada);
    }

    @Transactional
    public void atualizar(UUID idMetodoPagamento, MetodoPagamentoDto metodoPagamentoDto){
        var metodoPagamentoBancoOpt = repository.findByUuid(idMetodoPagamento);
        if(metodoPagamentoBancoOpt.isEmpty()){
            throw new ValidacaoException(TipoErroDespesa.NAO_ENCONTRADA);
        }
        var metodoPagamentoOpt = repository.findByPadraoIsTrue();
        if(Boolean.TRUE.equals(metodoPagamentoDto.padrao())
                && metodoPagamentoOpt.isPresent()
                && !metodoPagamentoOpt.get().getUuid().equals(idMetodoPagamento)){
            metodoPagamentoOpt.ifPresent(this::marcarComoNaoPadrao);
        }
        var metodoPagamentoBanco = metodoPagamentoBancoOpt.get();
        var novosDados = mapper.toEntity(metodoPagamentoDto);
        novosDados.setPadrao(Boolean.TRUE.equals(novosDados.getPadrao()));
        corrigirTipoMetodoPagamento(novosDados);
        corrigirTipoLancamentoCompetencia(novosDados);
        validarMetodoPagamento(metodoPagamentoDto);
        BeanUtils.copyProperties(novosDados, metodoPagamentoBanco, "id", "version", "uuid");
        repository.save(metodoPagamentoBanco);
    }

    @Transactional(readOnly = true)
    public boolean houveAlteracaoDiaVencimentoOuDiasFechamento(UUID idMetodoPagamento, MetodoPagamentoDto metodoPagamentoDto){
        var metodoPagamentoBancoOpt = repository.findByUuid(idMetodoPagamento);
        if(metodoPagamentoBancoOpt.isEmpty()){
            throw new ValidacaoException(TipoErroDespesa.NAO_ENCONTRADA);
        }
        var metodoPagamentoBanco = metodoPagamentoBancoOpt.get();
        return TipoMetodoPagamento.CARTAO_CREDITO.equals(metodoPagamentoDto.tipoMetodoPagamento())
                && (!metodoPagamentoDto.diaVencimento().equals(metodoPagamentoBanco.getDiaVencimento())
                || !metodoPagamentoDto.diasParaFechamento().equals(metodoPagamentoBanco.getDiasParaFechamento()));
    }

    private void validarNovoMetodoPagamento(MetodoPagamentoDto metodoPagamentoDto) {
        this.validarMetodoPagamento(metodoPagamentoDto);
        if(jaExiste(metodoPagamentoDto.nome())){
            throw new ValidacaoException(TipoErroMetodoPagamento.JA_EXISTE);
        }
    }

    private void validarMetodoPagamento(MetodoPagamentoDto metodoPagamentoDto) {
        if (StringUtils.isBlank(metodoPagamentoDto.nome())) {
            throw new ValidacaoException(TipoErroMetodoPagamento.NOME_NAO_INFORMADO);
        }
        if(Objects.nonNull(metodoPagamentoDto.diaVencimento())
                && naoEstaEntre(metodoPagamentoDto.diaVencimento(), 31)){
            throw new ValidacaoException(TipoErroMetodoPagamento.DIA_VENCIMENTO_FORA_DO_INTERVALO);
        }
        if(Objects.nonNull(metodoPagamentoDto.diasParaFechamento())
                && naoEstaEntre(metodoPagamentoDto.diasParaFechamento(), 15)){
            throw new ValidacaoException(TipoErroMetodoPagamento.DIAS_FECHAMENTO_FORA_DO_INTERVALO);
        }
    }

    private boolean jaExiste(@NonNull final String nome){
        var metodoPagamento = repository.findByNome(nome);
        return Objects.nonNull(metodoPagamento);
    }

    private boolean naoEstaEntre(int valor, int fim){
        return (valor < 1 || valor > fim);
    }

    private void corrigirTipoMetodoPagamento(MetodoPagamento metodoPagamento){
        if(Objects.nonNull(metodoPagamento.getDiaVencimento())
                && Objects.nonNull(metodoPagamento.getDiasParaFechamento())){
            metodoPagamento.setTipoMetodoPagamento(TipoMetodoPagamento.CARTAO_CREDITO);
        }else{
            metodoPagamento.setTipoMetodoPagamento(TipoMetodoPagamento.OUTROS);
        }
    }

    private void corrigirTipoLancamentoCompetencia(MetodoPagamento metodoPagamento){
        if(Objects.isNull(metodoPagamento.getTipoLancamentoCompetencia())){
            metodoPagamento.setTipoLancamentoCompetencia(TipoLancamentoCompetencia.DENTRO_MES);
        }
    }

    @Transactional
    public DatasResponse calcularDataVencimentoECompetencia(LocalDate dataLancamento, UUID guidFormaPagamento){
        var formaPagamentoOpt = repository.findByUuid(guidFormaPagamento);
        var dataVencimento = dataLancamento;
        var dataCompetencia = dataLancamento;
        var dataLimite = dataLancamento;
        if(formaPagamentoOpt.isPresent()){
            var formaPagamento = formaPagamentoOpt.get();
            dataVencimento = calcularDataVencimento(dataLancamento, formaPagamento.getDiaVencimento(), formaPagamento.getDiasParaFechamento());
            dataCompetencia = calcularDataCompetencia(dataVencimento, formaPagamento.getTipoLancamentoCompetencia());
            dataLimite = Objects.nonNull(formaPagamento.getDiasParaFechamento()) ? dataVencimento.minusDays(formaPagamento.getDiasParaFechamento()+1L):dataVencimento;
        }
        return new DatasResponse(dataVencimento, dataCompetencia, dataLimite);
    }

    /*private LocalDate calcularDataCompetencia(LocalDate dataVencimento, MetodoPagamento formaPagamento) {
        var tipoLancamentoCompetencia = Objects.nonNull(formaPagamento.getTipoLancamentoCompetencia()) ?
                formaPagamento.getTipoLancamentoCompetencia() : TipoLancamentoCompetencia.DENTRO_MES;
        switch (tipoLancamentoCompetencia){
            case ANTECIPADA -> {
                return dataVencimento.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1);
            }
            case POSTECIPADA -> {
                return dataVencimento.with(TemporalAdjusters.firstDayOfMonth()).plusMonths(1);
            }
            default -> {
                return dataVencimento.with(TemporalAdjusters.firstDayOfMonth());
            }
        }
    }

    public LocalDate calcularDataVencimento(LocalDate dataLancamento, MetodoPagamento formaPagamento){
        if(TipoMetodoPagamento.CARTAO_CREDITO.equals(formaPagamento.getTipoMetodoPagamento())){
            var diaBase = formaPagamento.getDiaVencimento() - formaPagamento.getDiasParaFechamento();
            if(diaBase <= 0){
                if(diaBase == 0){
                    return dataLancamento.plusMonths(1).withDayOfMonth(formaPagamento.getDiaVencimento());
                }else if (diaBase == -1) {
                    if(dataLancamento.getDayOfMonth() == dataLancamento.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()){
                        return dataLancamento.plusMonths(2).withDayOfMonth(formaPagamento.getDiaVencimento());
                    }else{
                        return dataLancamento.plusMonths(1).withDayOfMonth(formaPagamento.getDiaVencimento());
                    }
                } else{
                    var ultimoDiaMes = dataLancamento.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
                    var diaFechamento = ultimoDiaMes - Math.abs(diaBase);
                    if(dataLancamento.getDayOfMonth() >= diaFechamento){
                        return dataLancamento.plusMonths(2).withDayOfMonth(formaPagamento.getDiaVencimento());
                    }else{
                        return dataLancamento.plusMonths(1).withDayOfMonth(formaPagamento.getDiaVencimento());
                    }
                }
            }else{
                if(dataLancamento.getDayOfMonth() >= diaBase){
                    return dataLancamento.plusMonths(1).withDayOfMonth(formaPagamento.getDiaVencimento());
                }else{
                    return dataLancamento.withDayOfMonth(formaPagamento.getDiaVencimento());
                }
            }
        }
        return dataLancamento;
    }*/

    public DatasResponse calcularDataVencimentoECompetencia(LocalDate dataLancamento,
                                                            TipoLancamentoCompetencia tipoLancamentoCompetencia,
                                                            int diaVencimento, int diasFechamento){
        var dataVencimento = calcularDataVencimento(dataLancamento, diaVencimento == 0 ? null:diaVencimento, diasFechamento == 0 ? null:diasFechamento);
        var dataCompetencia = calcularDataCompetencia(dataLancamento, tipoLancamentoCompetencia);
        return new DatasResponse(dataVencimento, dataCompetencia, diaVencimento == 0 ? dataVencimento:dataVencimento.minusDays(diasFechamento+1));
    }

    public LocalDate calcularDataVencimento(LocalDate dataLancamento,
                                            Integer diaVencimento, Integer diasFechamento){
        if(Objects.nonNull(diaVencimento) && Objects.nonNull(diasFechamento)){
            var diaBase = diaVencimento - diasFechamento;
            if(diaBase <= 0){
                if(diaBase == 0){
                    return dataLancamento.plusMonths(1).withDayOfMonth(diaVencimento);
                }else if (diaBase == -1) {
                    if(dataLancamento.getDayOfMonth() == dataLancamento.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()){
                        return dataLancamento.plusMonths(2).withDayOfMonth(diaVencimento);
                    }else{
                        return dataLancamento.plusMonths(1).withDayOfMonth(diaVencimento);
                    }
                } else{
                    var ultimoDiaMes = dataLancamento.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
                    var diaFechamento = ultimoDiaMes - Math.abs(diaBase);
                    if(dataLancamento.getDayOfMonth() >= diaFechamento){
                        return dataLancamento.plusMonths(2).withDayOfMonth(diaVencimento);
                    }else{
                        return dataLancamento.plusMonths(1).withDayOfMonth(diaVencimento);
                    }
                }
            }else{
                if(dataLancamento.getDayOfMonth() >= diaBase){
                    return dataLancamento.plusMonths(1).withDayOfMonth(diaVencimento);
                }else{
                    return dataLancamento.withDayOfMonth(diaVencimento);
                }
            }
        }
        return dataLancamento;
    }

    private LocalDate calcularDataCompetencia(LocalDate dataVencimento, TipoLancamentoCompetencia tipoLancamentoCompetencia) {
        tipoLancamentoCompetencia = Objects.nonNull(tipoLancamentoCompetencia) ? tipoLancamentoCompetencia : TipoLancamentoCompetencia.DENTRO_MES;
        switch (tipoLancamentoCompetencia){
            case ANTECIPADA -> {
                return dataVencimento.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1);
            }
            case POSTECIPADA -> {
                return dataVencimento.with(TemporalAdjusters.firstDayOfMonth()).plusMonths(1);
            }
            default -> {
                return dataVencimento.with(TemporalAdjusters.firstDayOfMonth());
            }
        }
    }

    private MetodoPagamentoDto ajustaDespesaVinculada(MetodoPagamentoDto metodoPagamentoDto){
        var contemDespesa = contemDespesaVinculada(metodoPagamentoDto.id());
        return metodoPagamentoDto.withDespesaVinculada(contemDespesa);
    }

    private boolean contemDespesaVinculada(UUID metodoPagamentoId){
        return !despesaRepository
                .findAllByMetodoPagamento_Uuid( metodoPagamentoId)
                .isEmpty();
    }

}
