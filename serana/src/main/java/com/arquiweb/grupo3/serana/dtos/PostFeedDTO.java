package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostFeedDTO {

    private Long id;
    private String contenido;
    private String autor;
    private Long likes;
    private Long comentarios;
    private LocalDateTime fecha;
}
