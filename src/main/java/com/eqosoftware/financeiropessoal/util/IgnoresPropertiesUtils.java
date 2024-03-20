package com.eqosoftware.financeiropessoal.util;

import java.util.Arrays;
import java.util.List;

public class IgnoresPropertiesUtils {

    public static final List<String> BASE_ENTITY = Arrays.asList("id", "uuid", "version");
    public static final List<String> BASE_AUDITY = Arrays.asList("createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate");
    public static final List<String> BASE_RECOVER = Arrays.asList("deleted");
    public static final String[] ALL_BASE = new String[]{"id", "uuid", "version", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "deleted"};

    public static String[] ignorePropertiesAllBase(String... properties){
        int tamanhoTotal = properties.length + ALL_BASE.length;
        String[] resultado = new String[tamanhoTotal];
        System.arraycopy(properties, 0, resultado, 0, properties.length);
        System.arraycopy(ALL_BASE, 0, resultado, properties.length, ALL_BASE.length);
        return resultado;
    }

}
