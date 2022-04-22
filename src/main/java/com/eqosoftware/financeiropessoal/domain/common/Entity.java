package com.eqosoftware.financeiropessoal.domain.common;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by erik on 05/04/2022.
 */
public interface Entity extends Serializable {

    Long getId();

    void setId(final Long id);

    UUID getUuid();

    void setUuid(final UUID uuid);

    Integer getVersion();

    void setVersion(final Integer version);

}
