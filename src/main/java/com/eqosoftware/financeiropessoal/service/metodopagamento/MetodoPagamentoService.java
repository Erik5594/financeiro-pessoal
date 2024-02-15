package com.eqosoftware.financeiropessoal.service.metodopagamento;

import com.eqosoftware.financeiropessoal.domain.erro.TipoErroDespesa;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroMetodoPagamento;
import com.eqosoftware.financeiropessoal.dto.despesa.DespesaDto;
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

import java.util.Objects;
import java.util.UUID;

@Service
public class MetodoPagamentoService {

    @Autowired
    private MetodoPagamentoMapper metodoPagamentoMapper;

    @Autowired
    private MetodoPagamentoRepository metodoPagamentoRepository;

    public void criar(MetodoPagamentoDto metodoPagamentoDto){
        validarNovoMetodoPagamento(metodoPagamentoDto);
        var metodoPagamento = metodoPagamentoMapper.toEntity(metodoPagamentoDto);
        metodoPagamentoRepository.save(metodoPagamento);
    }

    public void remover(UUID uuidMetodoPagamento){
        var metodoPagamentoOpt = metodoPagamentoRepository.findByUuid(uuidMetodoPagamento);
        metodoPagamentoOpt.ifPresent(metodoPagamento -> metodoPagamentoRepository.delete(metodoPagamento));
    }

    public Page<MetodoPagamentoDto> listarMetodosPagamento(String nome, Pageable pageable){
        var metodosPagamento = StringUtils.isBlank(nome) ?
                metodoPagamentoRepository.findAll(pageable) :
                metodoPagamentoRepository.findAllByNomeIgnoreCaseContains(nome, pageable);
        return metodosPagamento.map(metodoPagamentoMapper::toDto);
    }

    public void atualizar(UUID idMetodoPagamento, MetodoPagamentoDto metodoPagamentoDto){
        var metodoPagamentoBancoOpt = metodoPagamentoRepository.findByUuid(idMetodoPagamento);
        if(metodoPagamentoBancoOpt.isEmpty()){
            throw new ValidacaoException(TipoErroDespesa.NAO_ENCONTRADA);
        }
        var metodoPagamentoBanco = metodoPagamentoBancoOpt.get();
        var novosDados = metodoPagamentoMapper.toEntity(metodoPagamentoDto);
        validarMetodoPagamento(metodoPagamentoDto);
        BeanUtils.copyProperties(novosDados, metodoPagamentoBanco, "id", "version", "uuid");
        metodoPagamentoRepository.save(metodoPagamentoBanco);
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
                && !estaEntre(metodoPagamentoDto.diaVencimento(), 1, 31)){
            throw new ValidacaoException(TipoErroMetodoPagamento.DIA_VENCIMENTO_FORA_DO_INTERVALO);
        }
        if(Objects.nonNull(metodoPagamentoDto.diasParaFechamento())
                && !estaEntre(metodoPagamentoDto.diasParaFechamento(), 1, 15)){
            throw new ValidacaoException(TipoErroMetodoPagamento.DIAS_FECHAMENTO_FORA_DO_INTERVALO);
        }
    }

    private boolean jaExiste(@NonNull final String nome){
        var metodoPagamento = metodoPagamentoRepository.findByNome(nome);
        return Objects.nonNull(metodoPagamento);
    }

    private boolean estaEntre(int valor, int inicio, int fim){
        return (valor > inicio && valor < fim);
    }

}
