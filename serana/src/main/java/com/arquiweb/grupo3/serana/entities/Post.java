package com.arquiweb.grupo3.serana.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tema;
    private String contenido;
    private Boolean modoAnonimo;
    private LocalDateTime fechaCreacion;
    private Integer cantidadParticipantes;

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<UsuarioPost> usuariosPosts;

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<Comentario> comentarios;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;
}
