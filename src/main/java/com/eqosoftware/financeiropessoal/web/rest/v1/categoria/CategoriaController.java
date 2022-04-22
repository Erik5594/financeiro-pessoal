package com.eqosoftware.financeiropessoal.web.rest.v1.categoria;

import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by erik on 28/01/2022.
 */

@RestController
@RequestMapping("/api/v1/categoria")
public class CategoriaController {

    @PostMapping
    public ResponseEntity<Void> criarCategoria(@RequestBody CategoriaDto categoriaDTO) {
        return ResponseEntity.ok().build();
    }

}
