package com.arquiweb.grupo3.serana.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sesiones_recursos_educativos")
public class SesionRecursoEducativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="id_sesion")
    private Sesion sesion;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="id_recurso_educativo")
    private RecursoEducativo recursoEducativo;
}
