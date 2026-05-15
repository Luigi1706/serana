package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.SesionRecursoEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SesionRecursoEducativoRepository extends JpaRepository<SesionRecursoEducativo, Long> {

    // Obtener todos los recursos educativos vinculados a una sesión
    @Query("SELECT sre FROM SesionRecursoEducativo sre WHERE sre.sesion.id = :sesionId")
    List<SesionRecursoEducativo> findBySesionId(Long sesionId);

    // Obtener todas las sesiones en que se usó un recurso educativo
    @Query("SELECT sre FROM SesionRecursoEducativo sre " +
            "WHERE sre.recursoEducativo.id = :recursoId")
    List<SesionRecursoEducativo> findByRecursoEducativoId(Long recursoId);

    // Verificar si ya existe el vínculo sesión-recurso (evitar duplicados)
    @Query("SELECT COUNT(sre) > 0 FROM SesionRecursoEducativo sre " +
            "WHERE sre.sesion.id = :sesionId AND sre.recursoEducativo.id = :recursoId")
    boolean existsBySesionIdAndRecursoEducativoId(Long sesionId, Long recursoId);

    // Contar cuántos recursos tiene una sesión
    @Query(value = "SELECT COUNT(*) FROM sesiones_recursos_educativos " +
            "WHERE id_sesion = :sesionId", nativeQuery = true)
    Long contarRecursosPorSesion(Long sesionId);
}
