package com.eqosoftware.financeiropessoal.web.rest.v1.perfil;

import com.eqosoftware.financeiropessoal.dto.perfil.PerfilDto;
import com.eqosoftware.financeiropessoal.dto.perfil.PerfilRequestAtualizaDto;
import com.eqosoftware.financeiropessoal.service.perfil.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @PutMapping
    public ResponseEntity<Void> atualizar(@RequestBody PerfilRequestAtualizaDto perfilDto) {
        perfilService.atualizar(perfilDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PerfilDto> buscar() {
        return ResponseEntity.ok(perfilService.buscarPerfilDto());
    }

    @GetMapping("/aux")
    public ResponseEntity<PerfilDto> buscarOuCriar() {
        return ResponseEntity.ok(perfilService.buscarOuCriarPerfil());
    }

    @PostMapping(value = "/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> alterarImagem(@RequestParam("file") MultipartFile file,
                                              @RequestHeader("X-Tenant-Id") String tenantId) {
        perfilService.guardarImagem(tenantId, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/imagem")
    public ResponseEntity<Void> removerImagem(@RequestHeader("X-Tenant-Id") String tenantId) {
        perfilService.removerImagem(tenantId);
        return ResponseEntity.ok().build();
    }

}
