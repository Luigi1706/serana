package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.PostDTO;
import com.arquiweb.grupo3.serana.entities.Post;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.repositories.PostRepository;
import com.arquiweb.grupo3.serana.services.PostService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UsuarioService usuarioService;

    @Override
    public Post add(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> listAll() {
        return postRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public PostDTO addDTO(PostDTO postDTO) {

        Usuario usuario = usuarioService.findById(postDTO.getUsuarioId());

        Post newPost = new Post(
                null,
                postDTO.getTema(),
                postDTO.getContenido(),
                postDTO.getModoAnonimo(),
                LocalDateTime.now(),
                postDTO.getCantidadParticipantes(),
                null,
                null,
                usuario
        );

        newPost = add(newPost);
        postDTO.setId(newPost.getId());
        return postDTO;
    }
}
