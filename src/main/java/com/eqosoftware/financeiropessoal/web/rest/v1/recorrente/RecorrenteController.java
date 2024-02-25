package com.eqosoftware.financeiropessoal.web.rest.v1.recorrente;

import com.eqosoftware.financeiropessoal.dto.recorrente.PreviaRecorrenciaDto;
import com.eqosoftware.financeiropessoal.dto.recorrente.RecorrenteRequestDto;
import com.eqosoftware.financeiropessoal.service.recorrente.RecorrenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recorrente")
public class RecorrenteController {

    @Autowired
    private RecorrenteService service;

    @GetMapping("/previas")
    public ResponseEntity<List<PreviaRecorrenciaDto>> buscar(RecorrenteRequestDto request) {
        return ResponseEntity.ok(service.gerarPrevias(request));
    }

}
