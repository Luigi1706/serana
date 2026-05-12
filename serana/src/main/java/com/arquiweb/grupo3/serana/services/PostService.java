package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.PostDTO;
import com.arquiweb.grupo3.serana.entities.Post;

import java.util.List;

public interface PostService {
    Post add(Post post);
    List<Post> listAll();
    void delete(Long id);
    Post findById(Long id);
    PostDTO addDTO(PostDTO postDTO);
}
