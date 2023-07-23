package com.eqosoftware.financeiropessoal.web.rest.v1.categoria;

import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaDto;
import com.eqosoftware.financeiropessoal.dto.categoria.CategoriaNaturezaTreeDto;
import com.eqosoftware.financeiropessoal.dto.categoria.FiltroDto;
import com.eqosoftware.financeiropessoal.service.categoria.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PutMapping("/{id}")
    public ResponseEntity<Void> editar(@PathVariable String id, @RequestBody CategoriaDto categoriaDTO) {
        categoriaService.atualizar(UUID.fromString(id), categoriaDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listar(FiltroDto filtroDto) {
        var categorias = categoriaService.listar(filtroDto);
        return ResponseEntity.ok(categorias.stream().map(categoriaService::acrescentarNomeCategoriaPai).toList());
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
