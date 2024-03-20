package com.eqosoftware.financeiropessoal.service.tenant;

import com.eqosoftware.financeiropessoal.domain.tenant.Tenant;
import com.eqosoftware.financeiropessoal.repository.tenant.TenantRepository;
import com.eqosoftware.financeiropessoal.service.schema.SchemaService;
import liquibase.integration.spring.MultiTenantSpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TenantService {

    private static final String NOME_NEW_TENANT = "tenant_%s_%s";
    private static final String EXPRESSAO_APENAS_LETRAS_E_NUMEROS = "[^0-9a-zA-Z_]";

    private final TenantRepository repository;
    private final SchemaService schemaService;
    private final MultiTenantSpringLiquibase multiTenantSpringLiquibase;

    @Autowired
    public TenantService(TenantRepository repository, SchemaService schemaService, MultiTenantSpringLiquibase multiTenantSpringLiquibase) {
        this.repository = repository;
        this.schemaService = schemaService;
        this.multiTenantSpringLiquibase = multiTenantSpringLiquibase;
    }

    public Tenant criarTenant(String username){
        var datatimeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
        var nomeSchema = String.format(NOME_NEW_TENANT, username.replaceAll(EXPRESSAO_APENAS_LETRAS_E_NUMEROS, ""), datatimeString);
        var tenant = new Tenant(nomeSchema);
        tenant = repository.saveAndFlush(tenant);
        criarSchema(nomeSchema);
        return tenant;
    }

    private void criarSchema(String nomeSchema){
        schemaService.criarSchemaParaUsuario(nomeSchema);

    }

    public void atualizarSchemas(){
        try {
            var tenants = getAllTenants();
            multiTenantSpringLiquibase.setSchemas(tenants);
            multiTenantSpringLiquibase.afterPropertiesSet();
        } catch (Exception e) {
            log.error("Ocorreu um erro ao executar o liquibase.", e);
        }
    }

    private List<String> getAllTenants(){
        var tenants = repository.findAll();
        return tenants.stream().map(Tenant::getNomeSchema).toList();
    }

}
