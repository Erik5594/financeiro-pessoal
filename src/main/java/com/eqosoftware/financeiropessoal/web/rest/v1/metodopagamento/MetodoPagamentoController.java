package com.eqosoftware.financeiropessoal.web.rest.v1.metodopagamento;

import com.eqosoftware.financeiropessoal.domain.metodopagamento.TipoLancamentoCompetencia;
import com.eqosoftware.financeiropessoal.dto.pagamento.DatasResponse;
import com.eqosoftware.financeiropessoal.dto.pagamento.MetodoPagamentoDto;
import com.eqosoftware.financeiropessoal.service.metodopagamento.MetodoPagamentoService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/metodo-pagamento")
public class MetodoPagamentoController {

    @Autowired
    private MetodoPagamentoService metodoPagamentoService;

    @GetMapping
    public ResponseEntity<Page<MetodoPagamentoDto>> listarMetodosPagamento(@RequestParam(value = "nome", required = false) String nome, Pageable page) {
        return ResponseEntity.ok(metodoPagamentoService.listarMetodosPagamento(nome, page));
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody MetodoPagamentoDto metodoPagamentoDto) {
        metodoPagamentoService.criar(metodoPagamentoDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuidMetodoPagamento}")
    public ResponseEntity<Void> deletar(@PathVariable UUID uuidMetodoPagamento) {
        metodoPagamentoService.remover(uuidMetodoPagamento);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editar(@PathVariable String id, @RequestBody MetodoPagamentoDto metodoPagamentoDto) {
        metodoPagamentoService.atualizar(UUID.fromString(id), metodoPagamentoDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{guidFormaPagamento}/dataVencimentoECompetencia")
    public ResponseEntity<DatasResponse> buscarDataVencimento(@PathVariable
                                                                  String guidFormaPagamento,
                                                              @RequestParam
                                                              @JsonFormat(pattern = "dd/MM/yyyy")
                                                              LocalDate dataBase){
        var datas = metodoPagamentoService.calcularDataVencimentoECompetencia(dataBase, UUID.fromString(guidFormaPagamento));
        return ResponseEntity.ok(datas);
    }

    @GetMapping("/dataVencimentoECompetencia")
    public ResponseEntity<DatasResponse> buscarDataVencimento(@RequestParam
                                                              @JsonFormat(pattern = "dd/MM/yyyy")
                                                              LocalDate dataBase,
                                                              @RequestParam
                                                              TipoLancamentoCompetencia tipoLancamentoCompetencia,
                                                              @RequestParam
                                                              Integer diaVencimento,
                                                              @RequestParam
                                                              Integer diasFechamento){
        var datas = metodoPagamentoService.calcularDataVencimentoECompetencia(dataBase, tipoLancamentoCompetencia, diaVencimento, diasFechamento);
        return ResponseEntity.ok(datas);
    }

}
