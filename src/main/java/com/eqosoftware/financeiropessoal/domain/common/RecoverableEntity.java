package com.eqosoftware.financeiropessoal.domain.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * Created by erik on 05/04/2022.
 */

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Audited
public abstract class RecoverableEntity extends AuditableEntity {

    private static final long serialVersionUID = 1L;

    public static final String NOT_DELETED = "deleted IS NULL";

    @Column(name = "deleted")
    private Instant deleted;

}
