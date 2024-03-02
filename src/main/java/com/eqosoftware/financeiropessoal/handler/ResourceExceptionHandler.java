package com.eqosoftware.financeiropessoal.handler;

import com.eqosoftware.financeiropessoal.domain.erro.DetalheErro;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransientPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DetalheErro> handleValidacaoException
            (ValidacaoException e, HttpServletRequest request) {

        DetalheErro erro = new DetalheErro(e.getMessage(), e.getStatus());

        return ResponseEntity.status(e.getStatus()).body(erro);
    }

    @ExceptionHandler({NullPointerException.class, TransientPropertyValueException.class})
    public ResponseEntity<DetalheErro> handleNullpointerException
            (Exception e, HttpServletRequest request) {

        log.error("Erro interno", e);

        DetalheErro erro = new DetalheErro(
                "Ocorreu um erro ao tentar processar sua requisição." +
                        " Tente novamente, caso persistir entre em contato com o suporte.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return ResponseEntity.status(erro.getStatus()).body(erro);
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<DetalheErro> handleAutenticacaoException
            (Exception e, HttpServletRequest request) {

        log.error("Erro ao autenticar", e);

        DetalheErro erro = new DetalheErro(
                e.getMessage(),
                HttpStatus.FORBIDDEN.value()
        );

        return ResponseEntity.status(erro.getStatus()).body(erro);
    }

}
