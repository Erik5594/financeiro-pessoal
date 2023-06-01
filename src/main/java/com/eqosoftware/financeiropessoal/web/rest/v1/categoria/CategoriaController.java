package com.eqosoftware.financeiropessoal.web.rest.v1.categoria;

import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaDto;
import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaNaturezaTreeDto;
import com.eqosoftware.financeiropessoal.service.categoria.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by erik on 28/01/2022.
 */

@RestController
@RequestMapping("/api/v1/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Void> criarCategoria(@RequestBody CategoriaDto categoriaDTO) {
        categoriaService.criar(categoriaDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @GetMapping("/tree")
    public ResponseEntity<CategoriaNaturezaTreeDto> buscarTodas() {
        return ResponseEntity.ok(categoriaService.buscarTodasToTree());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> buscar(@PathVariable String id) {
        return ResponseEntity.ok(categoriaService.buscar(UUID.fromString(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        categoriaService.deletar(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

}
