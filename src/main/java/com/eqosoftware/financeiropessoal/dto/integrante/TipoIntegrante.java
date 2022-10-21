package com.eqosoftware.financeiropessoal.dto.integrante;

public enum TipoIntegrante {

    PAI("Pai"),
    MAE("Mãe"),
    FILHO("Filho(a)"),
    NETO("Neto(a)"),
    TIO("Tio(a)"),
    SOBRINHO("Sobrinho(a)"),
    NORA("Nora"),
    GENRO("Genro"),
    AVO("Avô/Avó"),
    SOGRO("Sogro(a)"),
    IRMAO("Irmão/Irmã"),
    CUNHADO("Cunhado(a)"),
    PADASTRO("Padrastro/Madrastra");

    private String descricao;

    TipoIntegrante(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
