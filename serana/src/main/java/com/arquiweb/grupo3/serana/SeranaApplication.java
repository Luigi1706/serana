package com.arquiweb.grupo3.serana;

import com.arquiweb.grupo3.serana.entities.*;
import com.arquiweb.grupo3.serana.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class SeranaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeranaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			UsuarioRepository usuarioRepository,
			AuthorityRepository authorityRepository,
			PacienteRepository pacienteRepository,
			ProfesionalMedicoRepository profesionalRepository,
			ConfiguracionRepository configuracionRepository,
			PostRepository postRepository,
			ComentarioRepository comentarioRepository,
			UsuarioPostRepository usuarioPostRepository,
			UsuarioComentarioRepository usuarioComentarioRepository,
			SesionRepository sesionRepository,
			RecursosEducativosRepository recursoRepository,
			SesionRecursoEducativoRepository sesionRecursoRepository,
			HorarioRepository horarioRepository
	) {
		return args -> {

			// AUTORITIES
			Authority admin = authorityRepository.save(new Authority(null, "ROLE_ADMIN", null));
			Authority user = authorityRepository.save(new Authority(null, "ROLE_USER", null));
			Authority doctor = authorityRepository.save(new Authority(null, "ROLE_DOCTOR", null));

			// USUARIOS
			Usuario u1 = usuarioRepository.save(new Usuario(null, "ana@gmail.com", "1234",
					LocalDateTime.now(), true, null, null, null, null, null, List.of(user)));

			Usuario u2 = usuarioRepository.save(new Usuario(null, "carlos@gmail.com", "1234",
					LocalDateTime.now(), true, null, null, null, null, null, List.of(doctor)));

			Usuario u3 = usuarioRepository.save(new Usuario(null, "admin@gmail.com", "1234",
					LocalDateTime.now(), true, null, null, null, null, null, List.of(admin)));

			// CONFIGURACIONES
			configuracionRepository.save(new Configuracion(null, "oscuro", "es", true, u1));
			configuracionRepository.save(new Configuracion(null, "claro", "en", false, u2));

			// PACIENTES
			Paciente p1 = pacienteRepository.save(new Paciente(null, "Ana", "Lopez", 'F',
					false, "Ansiosa", 8, null, u1));

			// PROFESIONALES
			ProfesionalMedico prof1 = profesionalRepository.save(new ProfesionalMedico(
					null, "Carlos", "Perez", "Psicólogo", "Clínica San Pablo",
					null, null, u2
			));

			// HORARIOS
			horarioRepository.save(new Horario(null, "meet.com/abc1",
					LocalDate.now(), LocalTime.of(9, 0),
					LocalTime.of(10, 0), true, prof1));

			horarioRepository.save(new Horario(null, "meet.com/abc2",
					LocalDate.now().plusDays(1), LocalTime.of(11, 0),
					LocalTime.of(12, 0), true, prof1));

			// POSTS
			Post post1 = postRepository.save(new Post(null,
					"Ansiedad en exámenes",
					"Siento mucha ansiedad antes de mis exámenes, ¿algún consejo?",
					false,
					LocalDateTime.now(),
					1,
					null,
					null
			));

			Post post2 = postRepository.save(new Post(null,
					"Estrés laboral",
					"Mi trabajo me está agotando mentalmente",
					false,
					LocalDateTime.now(),
					2,
					null,
					null
			));

			// USUARIO - POST
			usuarioPostRepository.save(new UsuarioPost(null, true, true, u1, post1));
			usuarioPostRepository.save(new UsuarioPost(null, false, true, u1, post2));

			// COMENTARIOS
			Comentario c1 = comentarioRepository.save(new Comentario(null,
					LocalDateTime.now(),
					"Te recomiendo técnicas de respiración",
					null,
					post1
			));

			Comentario c2 = comentarioRepository.save(new Comentario(null,
					LocalDateTime.now(),
					"Podrías intentar meditación guiada",
					null,
					post2
			));

			// USUARIO - COMENTARIO
			usuarioComentarioRepository.save(new UsuarioComentario(null, true, true, u2, c1));
			usuarioComentarioRepository.save(new UsuarioComentario(null, true, false, u1, c2));

			// SESIONES
			Sesion s1 = sesionRepository.save(new Sesion(null,
					LocalDate.now(),
					LocalTime.of(10, 0),
					"Virtual",
					"Completada",
					"Paciente mostró mejora",
					p1,
					prof1,
					null
			));

			Sesion s2 = sesionRepository.save(new Sesion(null,
					LocalDate.now().minusDays(2),
					LocalTime.of(9, 0),
					"Presencial",
					"Pendiente",
					"Primera sesión",
					p1,
					prof1,
					null
			));

			// RECURSOS EDUCATIVOS
			RecursosEducativos r1 = recursoRepository.save(new RecursosEducativos(null,
					"Cómo manejar la ansiedad",
					"Video",
					"https://youtube.com/ansiedad",
					LocalDateTime.now(),
					null
			));

			RecursosEducativos r2 = recursoRepository.save(new RecursosEducativos(null,
					"Técnicas de respiración",
					"Artículo",
					"https://blog.com/respiracion",
					LocalDateTime.now(),
					null
			));

			// SESION - RECURSOS
			sesionRecursoRepository.save(new SesionRecursoEducativo(null, s1, r1));
			sesionRecursoRepository.save(new SesionRecursoEducativo(null, s1, r2));

		};
	}
}
