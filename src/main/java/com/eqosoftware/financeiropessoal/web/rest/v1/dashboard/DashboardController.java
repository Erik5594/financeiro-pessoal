package com.eqosoftware.financeiropessoal.web.rest.v1.dashboard;

import com.eqosoftware.financeiropessoal.dto.dashboard.TotalizadorDespesaPorSituacao;
import com.eqosoftware.financeiropessoal.service.despesa.DespesaService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DespesaService despesaService;

    @GetMapping
    public ResponseEntity<TotalizadorDespesaPorSituacao> buscarTotalizadorDespesa(
            @RequestParam(value = "competencia", required = false)
            @JsonFormat(pattern = "dd/MM/yyyy") LocalDate competencia) {
        return ResponseEntity.ok(despesaService.buscarTotalizadorPorCompetencia(competencia));
    }

}
