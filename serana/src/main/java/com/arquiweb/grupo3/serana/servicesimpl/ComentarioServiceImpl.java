package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.ComentarioDTO;
import com.arquiweb.grupo3.serana.entities.Comentario;
import com.arquiweb.grupo3.serana.entities.Post;
import com.arquiweb.grupo3.serana.repositories.ComentarioRepository;
import com.arquiweb.grupo3.serana.repositories.PostRepository;
import com.arquiweb.grupo3.serana.services.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    ComentarioRepository comentarioRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public Comentario findById(Long id) {
        return comentarioRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comentario> findByPostIdOrderByFecha(Long postId) {
        return comentarioRepository.findByPostIdOrderByFechaPublicacionAsc(postId);
    }

    @Override
    public List<Comentario> findComentariosConAutoresPorPostId(Long postId) {
        return comentarioRepository.findComentariosConAutoresPorPostId(postId);
    }

    @Override
    public Long contarComentariosPorPost(Long postId) {
        return comentarioRepository.contarComentariosSqlNativo(postId);
    }

    @Override
    public ComentarioDTO add(ComentarioDTO comentarioDTO) {
        Post post = postRepository.findById(comentarioDTO.getIdPost()).orElse(null);
        if(post == null){
            return null;
        }
        Comentario comentario = new Comentario();
        comentario.setContenido(comentarioDTO.getContenido());
        comentario.setFechaPublicacion(comentarioDTO.getFechaPublicacion()!=null?comentarioDTO.getFechaPublicacion(): LocalDateTime.now()
        );
        comentario.setPost(post);

        Comentario saved=comentarioRepository.save(comentario);
        ComentarioDTO dto=new ComentarioDTO();
        dto.setId(saved.getId());
        dto.setContenido(saved.getContenido());
        dto.setFechaPublicacion(saved.getFechaPublicacion());
        dto.setIdPost(saved.getPost().getId());
        dto.setIdUsuario(comentarioDTO.getIdUsuario());
        return dto;
    }

    @Override
    public void delete(Long id) {
        comentarioRepository.deleteById(id);
    }
}
