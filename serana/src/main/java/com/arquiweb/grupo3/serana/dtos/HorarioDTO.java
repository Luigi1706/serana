package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDTO {

    private Long id;

    private String link;

    private LocalDate fecha;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private Boolean disponible;

    private Long idProfesionalMedico;
}
