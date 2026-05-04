package com.arquiweb.grupo3.serana.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String correo;
    private String contrasenia;
    private LocalDateTime fechaRegistro;
    private Boolean habilitado;

    @JsonIgnore
    @OneToOne(mappedBy = "usuario", fetch= FetchType.EAGER)
    private ProfesionalMedico profesionalMedico;

    @JsonIgnore
    @OneToOne(mappedBy = "usuario", fetch=FetchType.EAGER)
    private Paciente paciente;

    @JsonIgnore
    @OneToOne(mappedBy = "usuario", fetch=FetchType.EAGER)
    private Configuracion configuracion;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", fetch=FetchType.EAGER)
    private List<UsuarioComentario> usuariosComentarios;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", fetch=FetchType.EAGER)
    private List<UsuarioPost> usuariosPosts;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="usuarios_authorities",
            joinColumns = {
                    @JoinColumn(
                            name="usuario_id",
                            referencedColumnName = "id",
                            nullable = false
                    )
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "authority_id",
                            referencedColumnName = "id",
                            nullable = false
                    )
            }
    )
    private List<Authority> authorities;
}
