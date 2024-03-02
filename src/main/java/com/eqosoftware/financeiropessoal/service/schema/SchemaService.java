package com.eqosoftware.financeiropessoal.service.schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SchemaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final String EXPRESSAO_APENAS_LETRAS_E_NUMEROS = "[^0-9a-zA-Z_]";

    public void criarSchemaParaUsuario(String schemaNome){
        var sql = "CREATE SCHEMA IF NOT EXISTS " + schemaNome.replaceAll(EXPRESSAO_APENAS_LETRAS_E_NUMEROS, "");
        jdbcTemplate.execute(sql);
    }

}
