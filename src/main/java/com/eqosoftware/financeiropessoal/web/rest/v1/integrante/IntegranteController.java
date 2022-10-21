package com.eqosoftware.financeiropessoal.web.rest.v1.integrante;


import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaDto;
import com.eqosoftware.financeiropessoal.dto.integrante.IntegranteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/integrante")
public class IntegranteController {

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody IntegranteDto integranteDto) {
        return ResponseEntity.ok().build();
    }

}
