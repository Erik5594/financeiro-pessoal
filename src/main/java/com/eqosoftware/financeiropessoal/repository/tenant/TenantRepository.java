package com.eqosoftware.financeiropessoal.repository.tenant;

import com.eqosoftware.financeiropessoal.domain.tenant.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
}
