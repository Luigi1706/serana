package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.UsuarioComentarioDTO;
import com.arquiweb.grupo3.serana.entities.Comentario;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.entities.UsuarioComentario;
import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.UsuarioComentarioRepository;
import com.arquiweb.grupo3.serana.services.ComentarioService;
import com.arquiweb.grupo3.serana.services.UsuarioComentarioService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioComentarioServiceImpl implements UsuarioComentarioService {

    @Autowired
    private UsuarioComentarioRepository usuarioComentarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ComentarioService comentarioService;

    @Override
    @Transactional
    public UsuarioComentarioDTO add(UsuarioComentarioDTO dto) {
        if (dto.getIdUsuario() == null) throw new ValidationException("El id del usuario es obligatorio.");
        if (dto.getIdComentario() == null) throw new ValidationException("El id del comentario es obligatorio.");

        // Evitar duplicados: un usuario no puede interactuar dos veces con el mismo comentario
        if (usuarioComentarioRepository.existeInteraccion(dto.getIdUsuario(), dto.getIdComentario()))
            throw new ValidationException("Ya existe una interacción de este usuario con el comentario " + dto.getIdComentario() + ".");

        Usuario usuario = usuarioService.findById(dto.getIdUsuario());
        Comentario comentario = comentarioService.findById(dto.getIdComentario());

        UsuarioComentario uc = new UsuarioComentario();
        uc.setUsuario(usuario);
        uc.setComentario(comentario);
        uc.setLiked(dto.getLiked() != null ? dto.getLiked() : false);
        uc.setComentarista(dto.getComentarista() != null ? dto.getComentarista() : false);

        uc = usuarioComentarioRepository.save(uc);
        dto.setId(uc.getId());
        return dto;
    }

    @Override
    public List<UsuarioComentarioDTO> findByUsuarioId(Long usuarioId) {
        usuarioService.findById(usuarioId); // valida existencia
        return usuarioComentarioRepository.findByUsuarioId(usuarioId)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<UsuarioComentarioDTO> findByComentarioId(Long comentarioId) {
        comentarioService.findById(comentarioId); // valida existencia
        return usuarioComentarioRepository.findByComentarioId(comentarioId)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public Long contarLikes(Long comentarioId) {
        return usuarioComentarioRepository.contarLikesPorComentario(comentarioId);
    }

    @Override
    @Transactional
    public UsuarioComentarioDTO toggleLike(Long usuarioId, Long comentarioId) {
        var existente = usuarioComentarioRepository.findByUsuarioIdAndComentarioId(usuarioId, comentarioId);
        if (existente.isPresent()) {
            UsuarioComentario uc = existente.get();
            uc.setLiked(!Boolean.TRUE.equals(uc.getLiked())); // toggle
            usuarioComentarioRepository.save(uc);
            return toDTO(uc);
        }
        // Crear nuevo registro de like
        UsuarioComentarioDTO dto = new UsuarioComentarioDTO(null, true, false, usuarioId, comentarioId);
        return add(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!usuarioComentarioRepository.existsById(id))
            throw new ResourceNotFoundException("Interacción usuario-comentario no encontrada con id: " + id);
        usuarioComentarioRepository.deleteById(id);
    }

    private UsuarioComentarioDTO toDTO(UsuarioComentario uc) {
        return new UsuarioComentarioDTO(
                uc.getId(),
                uc.getLiked(),
                uc.getComentarista(),
                uc.getUsuario().getId(),
                uc.getComentario().getId()
        );
    }
}
