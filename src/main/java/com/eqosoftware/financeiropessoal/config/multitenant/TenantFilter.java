package com.eqosoftware.financeiropessoal.config.multitenant;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var tenantName = httpRequest.getHeader("X-Tenant-Id");

        // Configura o tenant atual no TenantContext
        TenantContext.setCurrentTenant(tenantName);

        try {
            chain.doFilter(request, response);
        } finally {
            // Limpa o tenant após a solicitação
            TenantContext.clear();
        }
    }

}
