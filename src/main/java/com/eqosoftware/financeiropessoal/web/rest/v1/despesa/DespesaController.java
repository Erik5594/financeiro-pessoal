package com.eqosoftware.financeiropessoal.web.rest.v1.despesa;

import com.eqosoftware.financeiropessoal.dto.despesa.DespesaDto;
import com.eqosoftware.financeiropessoal.service.despesa.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/despesa")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @GetMapping
    public ResponseEntity<Page<DespesaDto>> listar(@RequestParam(value = "descricao", required = false) String descricao, Pageable page) {
        return ResponseEntity.ok(despesaService.listar(descricao, page));
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody DespesaDto despesa) {
        despesaService.criar(despesa);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        despesaService.deletar(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

}
