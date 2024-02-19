package com.eqosoftware.financeiropessoal.service.metodopagamento;

import com.eqosoftware.financeiropessoal.domain.erro.TipoErroDespesa;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroMetodoPagamento;
import com.eqosoftware.financeiropessoal.domain.metodopagamento.MetodoPagamento;
import com.eqosoftware.financeiropessoal.domain.metodopagamento.TipoMetodoPagamento;
import com.eqosoftware.financeiropessoal.dto.pagamento.MetodoPagamentoDto;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
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

import java.util.Objects;
import java.util.UUID;

@Service
public class MetodoPagamentoService {

    private final MetodoPagamentoMapper mapper;
    private final MetodoPagamentoRepository repository;

    @Autowired
    public MetodoPagamentoService(MetodoPagamentoMapper mapper, MetodoPagamentoRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
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
        repository.save(metodoPagamento);
    }

    private void marcarComoNaoPadrao(MetodoPagamento metodoPagamento){
        metodoPagamento.setPadrao(false);
        repository.save(metodoPagamento);
    }

    public void remover(UUID uuidMetodoPagamento){
        var metodoPagamentoOpt = repository.findByUuid(uuidMetodoPagamento);
        metodoPagamentoOpt.ifPresent(metodoPagamento -> repository.delete(metodoPagamento));
    }

    public Page<MetodoPagamentoDto> listarMetodosPagamento(String nome, Pageable pageable){
        var metodosPagamento = StringUtils.isBlank(nome) ?
                repository.findAll(pageable) :
                repository.findAllByNomeIgnoreCaseContains(nome, pageable);
        metodosPagamento.forEach(this::corrigirTipoMetodoPagamento);
        return metodosPagamento.map(mapper::toDto);
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
        validarMetodoPagamento(metodoPagamentoDto);
        BeanUtils.copyProperties(novosDados, metodoPagamentoBanco, "id", "version", "uuid");
        repository.save(metodoPagamentoBanco);
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

}
