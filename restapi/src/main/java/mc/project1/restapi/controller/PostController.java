package mc.project1.restapi.controller;

import lombok.RequiredArgsConstructor;
import mc.project1.restapi.model.Post;
import mc.project1.restapi.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController
{
    private final PostService postService;

    @GetMapping("/posts")
    public List<Post> getPosts()
    {
        return postService.getPosts();
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id)
    {
        return postService.getPost(id);
    }
}
