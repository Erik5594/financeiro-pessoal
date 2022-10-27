package com.eqosoftware.financeiropessoal.domain.erro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalheErro {

    private String mensagem;
    private Long timestamp;
    private int status;

    public DetalheErro(String mensagem, int status) {
        this.mensagem = mensagem;
        this.timestamp = System.currentTimeMillis();
        this.status = status;
    }


}
