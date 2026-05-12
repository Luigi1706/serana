package com.arquiweb.grupo3.serana.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;

    private String correo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasenia;

    private LocalDateTime fechaRegistro;

    private String authorities;
}
