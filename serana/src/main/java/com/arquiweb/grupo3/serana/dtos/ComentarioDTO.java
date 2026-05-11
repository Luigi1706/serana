package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ComentarioDTO {

    private Long id;
    private LocalDateTime fechaPublicacion;
    private String contenido;
    private Long idPost;
    private Long idUsuario;
}