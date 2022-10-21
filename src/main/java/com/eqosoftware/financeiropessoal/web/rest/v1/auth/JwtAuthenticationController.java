package com.eqosoftware.financeiropessoal.web.rest.v1.auth;

import com.eqosoftware.financeiropessoal.config.jwts.JwtTokenUtil;
import com.eqosoftware.financeiropessoal.config.security.AppUserDetailsService;
import com.eqosoftware.financeiropessoal.domain.auth.GrupoAcesso;
import com.eqosoftware.financeiropessoal.domain.auth.Usuario;
import com.eqosoftware.financeiropessoal.dto.grupoacesso.GrupoAcessoDto;
import com.eqosoftware.financeiropessoal.dto.token.JwtResponseDto;
import com.eqosoftware.financeiropessoal.dto.token.UsuarioDto;
import com.eqosoftware.financeiropessoal.service.usuario.UsuarioService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping(value = "/signup")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UsuarioDto usuarioDto) {
        UsuarioDto usuario = usuarioService.criarUsuario(usuarioDto);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> listar() {
        List<UsuarioDto> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping(value = "/role")
    public ResponseEntity<?> listarRoles() {
        List<GrupoAcessoDto> gruposAcesso = usuarioService.listarGrupoAcesso();
        return ResponseEntity.ok(gruposAcesso);
    }

    @PostMapping(value = "/role")
    public ResponseEntity<?> createGrupoAcesso(@RequestBody GrupoAcessoDto grupoAcessoDto, HttpServletRequest request) {
        GrupoAcessoDto grupoAcesso = usuarioService.criarGrupoAcesso(grupoAcessoDto);
        return ResponseEntity.created(URI.create(request.getRequestURI() + "/" + grupoAcesso.getId())).body(grupoAcesso);
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<?> authenticationToken(@RequestBody UsuarioDto usuario) throws Exception {
        String username = StringUtils.isBlank(usuario.getUsername()) ? usuario.getEmail():usuario.getUsername();

        authenticate(username, usuario.getSenha());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
