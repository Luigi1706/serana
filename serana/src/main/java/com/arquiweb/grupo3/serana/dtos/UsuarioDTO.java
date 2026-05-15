package com.arquiweb.grupo3.serana.dtos;

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
    private String contrasenia;
    private LocalDateTime fechaRegistro;
    private String authorities;

    /**
     * Tipo de perfil que se creará automáticamente al registrar.
     * Valores válidos: "PACIENTE" | "DOCTOR"
     * Determina el rol asignado automáticamente (ROLE_PACIENTE o ROLE_DOCTOR).
     */
    private String tipoPerfil;
}
