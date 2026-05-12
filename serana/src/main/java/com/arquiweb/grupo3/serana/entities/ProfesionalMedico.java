package com.arquiweb.grupo3.serana.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="profesionales_medicos")
public class ProfesionalMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;
    private String apellidos;
    private String especialidad;
    private String lugarTrabajo;

    @JsonIgnore
    @OneToMany(mappedBy = "profesionalMedico")
    private List<Sesion> sesiones;

    @JsonIgnore
    @OneToMany(mappedBy = "profesionalMedico", fetch = FetchType.EAGER)
    private List<Horario> horarios;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;
}
