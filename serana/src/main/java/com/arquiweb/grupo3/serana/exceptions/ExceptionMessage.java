package com.arquiweb.grupo3.serana.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {

    private int status;  // que codigo de estatus se genero
    private String exception;  // que exception se levanto
    private String message;  // descripcion particular de el error
    private String requestDescription;  // cual fue el request que genero el error
    private LocalDateTime timestamp;  // cuando se genero el error

}
