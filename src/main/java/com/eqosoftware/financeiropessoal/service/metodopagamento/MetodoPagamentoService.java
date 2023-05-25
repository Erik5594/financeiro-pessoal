package com.eqosoftware.financeiropessoal.service.metodopagamento;

import com.eqosoftware.financeiropessoal.domain.erro.TipoErroMetodoPagamento;
import com.eqosoftware.financeiropessoal.dto.pagamento.MetodoPagamentoDto;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
import com.eqosoftware.financeiropessoal.repository.metodopagamento.MetodoPagamentoRepository;
import com.eqosoftware.financeiropessoal.service.metodopagamento.mapper.MetodoPagamentoMapper;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public void remover(long idMetodoPagamento){
        var metodoPagamentoOpt = metodoPagamentoRepository.findById(idMetodoPagamento);
        if(metodoPagamentoOpt.isPresent()){
            metodoPagamentoRepository.delete(metodoPagamentoOpt.get());
        }
    }

    public Page<MetodoPagamentoDto> listarMetodosPagamento(String nome, Pageable pageable){
        var metodosPagamento = StringUtils.isBlank(nome) ?
                metodoPagamentoRepository.findAll(pageable) :
                metodoPagamentoRepository.findAllByNomeIgnoreCaseContains(nome, pageable);
        return metodosPagamento.map(metodoPagamentoMapper::toDto);
    }

    private void validarNovoMetodoPagamento(MetodoPagamentoDto metodoPagamentoDto) {
        if (StringUtils.isBlank(metodoPagamentoDto.getNome())) {
            throw new ValidacaoException(TipoErroMetodoPagamento.NOME_NAO_INFORMADO);
        }
        if(jaExiste(metodoPagamentoDto.getNome())){
            throw new ValidacaoException(TipoErroMetodoPagamento.JA_EXISTE);
        }
    }

    private boolean jaExiste(@NonNull final String nome){
        var metodoPagamento = metodoPagamentoRepository.findByNome(nome);
        return Objects.nonNull(metodoPagamento);
    }

}
