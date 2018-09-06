package com.syswin.temail.media.bank.exception;

import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefineExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handler(Exception e){
        if(e instanceof DefineException){
            DefineException defineException = (DefineException)e;
            return new ResponseEntity<>(defineException.getMsg(), HttpStatus.valueOf(defineException.getCode()));
        }
        return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(ResponseCodeConstants.SERVER_ERROR));
    }
}