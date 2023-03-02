package com.wikipedia.wikipediasearchservice.handler;

import com.wikipedia.wikipediasearchservice.dto.error.NoFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ErrorControllerAdvice {


    // BAD_REQUEST handlers


//    @ExceptionHandler(EntityNotFoundException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public NoFieldException onEntityNotFoundException(
//            Exception e
//    ) {
//        return new NoFieldException(
//                e.getMessage()
//        );
//    }
//
//
//    // INTERNAL_SERVER_ERROR handlers
//
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public NoFieldException onException(
//            Exception e
//    ) {
//        return new NoFieldException(
//                e.getMessage()
//        );
//    }

}
