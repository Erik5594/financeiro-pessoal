package com.eqosoftware.financeiropessoal.exceptions;

import com.eqosoftware.financeiropessoal.domain.erro.Erro;
import com.eqosoftware.financeiropessoal.domain.erro.TipoErroGrupoAcesso;

public class ValidacaoException  extends RuntimeException {

    private int status;

    public ValidacaoException(String mensagem, int status) {
        super(mensagem);
        this.status = status;
    }

    public ValidacaoException(Erro erro) {
        super(erro.getDescricaoErro());
        this.status = erro.getStatusHttp();
    }

    public int getStatus() {
        return status;
    }
}
