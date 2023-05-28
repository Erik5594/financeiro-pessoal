package com.eqosoftware.financeiropessoal.repository.auditingconfig;

import com.eqosoftware.financeiropessoal.config.security.UsuarioSistema;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuditingService implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(getCurrentUserLogin());
    }

    public static String getCurrentUserLogin() {
        var usuarioLogado = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Objects.isNull(usuarioLogado) ? "":((UsuarioSistema) usuarioLogado).getUsername();
    }
}
