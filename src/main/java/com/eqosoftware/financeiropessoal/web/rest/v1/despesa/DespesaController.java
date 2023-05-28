package com.eqosoftware.financeiropessoal.web.rest.v1.despesa;

import com.eqosoftware.financeiropessoal.dto.despesa.DespesaDto;
import com.eqosoftware.financeiropessoal.service.despesa.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/despesa")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody DespesaDto despesa) {
        despesaService.criar(despesa);
        return ResponseEntity.ok().build();
    }

}
