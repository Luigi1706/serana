package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Se usa en UserDetailsService para autenticar usuario
    Usuario findByCorreo(String correo); // Query Method

    //Carga usuario + roles en una sola consulta
    @Query("SELECT u FROM Usuario u JOIN FETCH u.authorities WHERE u.correo = ?1")
    Usuario obtenerUsuarioConRoles(String email); // JPQL

    // Evita duplicados en registros
    boolean existsByCorreo(String correo); // Query Method
}
