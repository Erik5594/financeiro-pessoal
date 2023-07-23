package com.eqosoftware.financeiropessoal.dto.categoria;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

public record FiltroDto(@RequestParam("natureza") TipoNatureza natureza,
                        @RequestParam("nome") String nome,
                        @RequestParam("ultimaFilha") Boolean ultimaFilha) {

    public boolean semFiltro(){
        return Objects.isNull(this.natureza)
                && StringUtils.isBlank(this.nome)
                && !Boolean.TRUE.equals(this.ultimaFilha);
    }
}
