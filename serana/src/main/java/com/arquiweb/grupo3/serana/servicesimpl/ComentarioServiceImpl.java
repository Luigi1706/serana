package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.ComentarioDTO;
import com.arquiweb.grupo3.serana.entities.Comentario;
import com.arquiweb.grupo3.serana.entities.Post;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.repositories.ComentarioRepository;
import com.arquiweb.grupo3.serana.services.ComentarioService;
import com.arquiweb.grupo3.serana.services.PostService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PostService postService;

    @Override
    public Comentario add(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    @Override
    public List<Comentario> listAll() {
        return comentarioRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        comentarioRepository.deleteById(id);
    }

    @Override
    public Comentario findById(Long id) {
        return comentarioRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comentario> listByPostIdOrderByFecha(Long postId) {
        return comentarioRepository.findByPostIdOrderByFechaPublicacionAsc(postId);
    }

    @Override
    public Long contarComentariosDePost(Long postId) {
        return comentarioRepository.contarComentariosDePost(postId);
    }

    @Override
    public ComentarioDTO addDTO(ComentarioDTO comentarioDTO) {

        Post post = postService.findById(comentarioDTO.getPostId());

        Usuario usuario = usuarioService.findById(comentarioDTO.getUsuarioId());

        Comentario newComentario = new Comentario(
                null,
                comentarioDTO.getFechaPublicacion(),
                comentarioDTO.getContenido(),
                null,
                post,
                usuario
        );

        newComentario = add(newComentario);
        comentarioDTO.setId(newComentario.getId());
        return comentarioDTO;
    }
}
