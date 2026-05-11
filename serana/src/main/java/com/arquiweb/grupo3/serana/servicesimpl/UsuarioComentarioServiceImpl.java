/*package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.UsuarioComentarioDTO;
import com.arquiweb.grupo3.serana.entities.Comentario;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.entities.UsuarioComentario;
//import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.UsuarioComentarioRepository;
import com.arquiweb.grupo3.serana.services.ComentarioService;
import com.arquiweb.grupo3.serana.services.UsuarioComentarioService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioComentarioServiceImpl implements UsuarioComentarioService {

    @Autowired
    UsuarioComentarioRepository usuarioComentarioRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ComentarioService comentarioService;

    @Override
    public UsuarioComentario findById(Long id) {
        return usuarioComentarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el registro usuario-comentario con id: " + id));
    }

    @Override
    public List<UsuarioComentarioDTO> findByComentarioId(Long comentarioId) {
        comentarioService.findById(comentarioId);
        return usuarioComentarioRepository.findByComentarioId(comentarioId)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<UsuarioComentarioDTO> findByUsuarioId(Long usuarioId) {
        usuarioService.findById(usuarioId);
        return usuarioComentarioRepository.findByUsuarioId(usuarioId)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public Long contarLikesPorComentario(Long comentarioId) {
        comentarioService.findById(comentarioId);
        return usuarioComentarioRepository.contarLikesPorComentario(comentarioId);
    }

    @Override
    public UsuarioComentarioDTO darLike(Long usuarioId, Long comentarioId) {
        usuarioService.findById(usuarioId);
        comentarioService.findById(comentarioId);


        if (usuarioComentarioRepository.usuarioYaDioLike(usuarioId, comentarioId)) {
            throw new ValidationException(
                    "El usuario ya le dio like a este comentario.");
        }

        UsuarioComentario usuarioComentario = usuarioComentarioRepository
                .findByUsuarioIdAndComentarioId(usuarioId, comentarioId)
                .orElse(null);

        if (usuarioComentario != null) {
            usuarioComentario.setLiked(true);
        } else {
            Usuario usuario       = usuarioService.findById(usuarioId);
            Comentario comentario = comentarioService.findById(comentarioId);
            usuarioComentario = new UsuarioComentario(null, true, false, usuario, comentario);
        }

        usuarioComentario = usuarioComentarioRepository.save(usuarioComentario);
        UsuarioComentarioDTO dto = toDTO(usuarioComentario);
        dto.setTotalLikes(usuarioComentarioRepository.contarLikesPorComentario(comentarioId));
        return dto;
    }

    @Override
    public UsuarioComentarioDTO quitarLike(Long usuarioId, Long comentarioId) {
        usuarioService.findById(usuarioId);
        comentarioService.findById(comentarioId);

        UsuarioComentario usuarioComentario = usuarioComentarioRepository
                .findByUsuarioIdAndComentarioId(usuarioId, comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El usuario no tiene registro con este comentario."));


        if (!Boolean.TRUE.equals(usuarioComentario.getLiked())) {
            throw new ValidationException(
                    "El usuario no ha dado like a este comentario.");
        }

        usuarioComentario.setLiked(false);
        usuarioComentario = usuarioComentarioRepository.save(usuarioComentario);
        UsuarioComentarioDTO dto = toDTO(usuarioComentario);
        dto.setTotalLikes(usuarioComentarioRepository.contarLikesPorComentario(comentarioId));
        return dto;
    }

    @Override
    public UsuarioComentarioDTO add(UsuarioComentarioDTO dto) {

        if (dto.getIdUsuario() == null) {
            throw new ValidationException("Debe indicar el id del usuario.");
        }
        if (dto.getIdComentario() == null) {
            throw new ValidationException("Debe indicar el id del comentario.");
        }

        Usuario usuario         = usuarioService.findById(dto.getIdUsuario());
        Comentario comentario   = comentarioService.findById(dto.getIdComentario());

        UsuarioComentario nuevo = new UsuarioComentario(
                null,
                dto.getLiked()        != null ? dto.getLiked()        : false,
                dto.getComentarista() != null ? dto.getComentarista() : false,
                usuario,
                comentario
        );
        nuevo = usuarioComentarioRepository.save(nuevo);
        return toDTO(nuevo);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        usuarioComentarioRepository.deleteById(id);
    }

    private UsuarioComentarioDTO toDTO(UsuarioComentario uc) {
        UsuarioComentarioDTO dto = new UsuarioComentarioDTO();
        dto.setId(uc.getId());
        dto.setLiked(uc.getLiked());
        dto.setComentarista(uc.getComentarista());
        dto.setIdUsuario(uc.getUsuario().getId());
        dto.setIdComentario(uc.getComentario().getId());
        dto.setCorreoUsuario(uc.getUsuario().getCorreo());
        dto.setContenidoComentario(uc.getComentario().getContenido());
        return dto;
    }

}
*/