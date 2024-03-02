package com.eqosoftware.financeiropessoal.web.rest.v1.auth;

import com.eqosoftware.financeiropessoal.config.jwts.JwtTokenUtil;
import com.eqosoftware.financeiropessoal.config.security.AppUserDetailsService;
import com.eqosoftware.financeiropessoal.config.security.UsuarioSistema;
import com.eqosoftware.financeiropessoal.dto.grupoacesso.GrupoAcessoDto;
import com.eqosoftware.financeiropessoal.dto.token.JwtResponseDto;
import com.eqosoftware.financeiropessoal.dto.token.UsuarioDto;
import com.eqosoftware.financeiropessoal.service.tenant.TenantService;
import com.eqosoftware.financeiropessoal.service.usuario.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Created by erik on 28/01/2022.
 */

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TenantService tenantService;

    @PostMapping(value = "/signup")
    public ResponseEntity<JwtResponseDto> createAuthenticationToken(@RequestBody UsuarioDto usuarioDto) throws Exception {
        var senha = usuarioDto.getSenha();
        UsuarioDto usuario = usuarioService.criarUsuario(usuarioDto);
        usuario.setSenha(senha);
        tenantService.atualizarSchemas();
        return autenticar(usuario);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDto>> listar() {
        List<UsuarioDto> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping(value = "/role")
    public ResponseEntity<List<GrupoAcessoDto>> listarRoles() {
        List<GrupoAcessoDto> gruposAcesso = usuarioService.listarGrupoAcesso();
        return ResponseEntity.ok(gruposAcesso);
    }

    @PostMapping(value = "/role")
    public ResponseEntity<GrupoAcessoDto> createGrupoAcesso(@RequestBody GrupoAcessoDto grupoAcessoDto, HttpServletRequest request) {
        GrupoAcessoDto grupoAcesso = usuarioService.criarGrupoAcesso(grupoAcessoDto);
        return ResponseEntity.created(URI.create(request.getRequestURI() + "/" + grupoAcesso.getId())).body(grupoAcesso);
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<JwtResponseDto> authenticationToken(@RequestBody UsuarioDto usuario) throws Exception {
        return autenticar(usuario);
    }

    private ResponseEntity<JwtResponseDto> autenticar(UsuarioDto usuario) throws Exception {
        String username = StringUtils.isBlank(usuario.getUsername()) ? usuario.getEmail():usuario.getUsername();

        authenticate(username, usuario.getSenha());

        final UsuarioSistema usuarioSistema = userDetailsService
                .findByUsername(username);
        final String token = jwtTokenUtil.generateToken(usuarioSistema);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }
    }

}
