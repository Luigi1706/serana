package com.arquiweb.grupo3.serana.controllers;

import com.arquiweb.grupo3.serana.dtos.PostDTO;
import com.arquiweb.grupo3.serana.entities.Post;
import com.arquiweb.grupo3.serana.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana") // http://localhost:8080/arqui_serana
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("/posts") // http://localhost:8080/arqui_serana/posts
    public ResponseEntity<List<Post>> listAll(){
        return new ResponseEntity<>(postService.listAll(), HttpStatus.OK);
    }

    @PostMapping("/posts") // http://localhost:8080/arqui_serana/posts
    public ResponseEntity<PostDTO> create(@RequestBody PostDTO postDTO){
        PostDTO newPostDTO = postService.addDTO(postDTO);
        return new ResponseEntity<>(newPostDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("posts/{postId}") // http://localhost:8080/arqui_serana/posts/1
    public ResponseEntity<HttpStatus> delete(@PathVariable("postId") Long postId){
        postService.delete(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
