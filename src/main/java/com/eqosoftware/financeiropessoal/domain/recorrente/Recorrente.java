package com.eqosoftware.financeiropessoal.domain.recorrente;

import com.eqosoftware.financeiropessoal.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
public class Recorrente extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private TipoFrequencia frequencia;
    private LocalDate dataLimite;

}
