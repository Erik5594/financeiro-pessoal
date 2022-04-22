package com.eqosoftware.financeiropessoal.dto.token;

import lombok.Data;

/**
 * Created by erik on 28/01/2022.
 */

@Data
public class JwtResponseDto {
    private final String jwttoken;

    public JwtResponseDto(String jwttoken) {
        this.jwttoken = jwttoken;
    }

}
