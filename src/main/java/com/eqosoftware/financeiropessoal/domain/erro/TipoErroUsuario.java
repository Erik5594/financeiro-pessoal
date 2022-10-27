package com.eqosoftware.financeiropessoal.domain.erro;

public enum TipoErroUsuario implements Erro {

    NOME_NAO_INFORMADO("Deve ser informado um nome para o novo usuário.", 400),
    USERNAME_NAO_INFORMADO("Deve ser informado um username para o novo usuário.", 400),
    EMAIL_NAO_INFORMADO("Deve ser informado um email para o novo usuário.", 400),
    SENHA_NAO_INFORMADO("Deve ser informado uma senha para o novo usuário.", 400),
    USERNAME_JA_EXISTE("Username já cadastrado. Informe outro ou realize o login.", 400),
    EMAIL_JA_EXISTE("E-mail já cadastrado. Informe outro ou realize o login", 400),
    SENHA_CARACTERES_MINIMO_NAO_ATINGIDO("Quantidade mínima de caracteres não atingida. Mínimo[6].", 400),
    EMAIL_INVALIDO("O e-mail informado é inválido.", 400);

    private String descricaoErro;
    private int statusHttp;

    TipoErroUsuario(String descricaoErro, int statusHttp) {
        this.descricaoErro = descricaoErro;
        this.statusHttp = statusHttp;
    }

    public String getDescricaoErro() {
        return descricaoErro;
    }

    public int getStatusHttp() {
        return statusHttp;
    }
}
