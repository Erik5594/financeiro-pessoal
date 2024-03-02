package com.eqosoftware.financeiropessoal.domain.tenant;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(schema = "public")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeSchema;

    public Tenant(String nomeSchema) {
        this.nomeSchema = nomeSchema;
    }
}
