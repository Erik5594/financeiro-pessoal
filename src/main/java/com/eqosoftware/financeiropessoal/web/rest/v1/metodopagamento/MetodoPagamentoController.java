package com.eqosoftware.financeiropessoal.web.rest.v1.metodopagamento;

import com.eqosoftware.financeiropessoal.dto.pagamento.MetodoPagamentoDto;
import com.eqosoftware.financeiropessoal.service.metodopagamento.MetodoPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
