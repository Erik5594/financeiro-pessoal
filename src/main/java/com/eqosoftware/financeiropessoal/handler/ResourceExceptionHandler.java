package com.eqosoftware.financeiropessoal.handler;

import com.eqosoftware.financeiropessoal.domain.erro.DetalheErro;
import com.eqosoftware.financeiropessoal.exceptions.ValidacaoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DetalheErro> handleValidacaoException
            (ValidacaoException e, HttpServletRequest request) {

        DetalheErro erro = new DetalheErro(e.getMessage(), e.getStatus());

        return ResponseEntity.status(e.getStatus()).body(erro);
    }

}
