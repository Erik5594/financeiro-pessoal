package com.eqosoftware.financeiropessoal.domain.common;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by erik on 28/01/2022.
 */
@Data
@MappedSuperclass
public abstract class BaseEntity implements Entity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Column(nullable = false, unique = true, insertable = true, updatable = false)
    private UUID uuid;

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(getUuid())) {
            setUuid(UUID.randomUUID());
        }
    }

}
