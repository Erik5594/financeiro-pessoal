package com.eqosoftware.financeiropessoal.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by erik on 28/01/2022.
 */

@Configuration
public class PasswordEncoderConfiguration {

    //NAO ALTERAR ESTE VALOR, POIS ISTO INVALIDARA AS SENHAS SALVAS
    private static final int BCRYPT_PASSWORD_STRENGTH = 7;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_PASSWORD_STRENGTH);
    }

}
