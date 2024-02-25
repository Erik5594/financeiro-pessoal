package com.eqosoftware.financeiropessoal.service.recorrente;

import com.eqosoftware.financeiropessoal.domain.recorrente.Recorrente;
import com.eqosoftware.financeiropessoal.dto.recorrente.PreviaRecorrenciaDto;
import com.eqosoftware.financeiropessoal.dto.recorrente.RecorrenteDto;
import com.eqosoftware.financeiropessoal.dto.recorrente.RecorrenteRequestDto;
import com.eqosoftware.financeiropessoal.repository.recorrente.RecorrenteRepository;
import com.eqosoftware.financeiropessoal.service.despesa.mapper.RecorrenteMapper;
import com.eqosoftware.financeiropessoal.service.metodopagamento.MetodoPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecorrenteService {

    private final RecorrenteRepository repository;
    private final RecorrenteMapper mapper;
    private final MetodoPagamentoService metodoPagamentoService;

    @Autowired
    public RecorrenteService(RecorrenteRepository repository, RecorrenteMapper mapper, MetodoPagamentoService metodoPagamentoService) {
        this.repository = repository;
        this.mapper = mapper;
        this.metodoPagamentoService = metodoPagamentoService;
    }

    public List<PreviaRecorrenciaDto> gerarPrevias(RecorrenteRequestDto request) {
        var primeirasDatas = metodoPagamentoService.calcularDataVencimentoECompetencia(request.primeiroLancamento(), UUID.fromString(request.uuidFormaPagamento()));
        var previas = new ArrayList<PreviaRecorrenciaDto>();
        var vencimento = primeirasDatas.dataVencimento();
        var lancamento = request.primeiroLancamento();
        var competencia = primeirasDatas.dataCompetencia();
        var count = 0;
        while (!request.dataLimite().isBefore(lancamento)){
            previas.add(new PreviaRecorrenciaDto(count, competencia, lancamento, vencimento));
            switch (request.frequencia()){
                case DIARIA -> lancamento = lancamento.plusDays(1);
                case SEMANAL -> lancamento = lancamento.plusWeeks(1);
                case QUINZENAL -> lancamento = lancamento.plusDays(15);
                case MENSAL -> lancamento = lancamento.plusMonths(1);
            }
            var datas = metodoPagamentoService.calcularDataVencimentoECompetencia(lancamento, UUID.fromString(request.uuidFormaPagamento()));
            vencimento = datas.dataVencimento();
            competencia = datas.dataCompetencia();
            count++;
        }
        return previas;
    }

    public Recorrente criar(RecorrenteDto recorrenteDto){
        var recorrencia = mapper.toEntity(recorrenteDto);
        return repository.saveAndFlush(recorrencia);
    }
}
