package mc.project1.restapi.controller;

import lombok.RequiredArgsConstructor;
import mc.project1.restapi.dto.PostDto;
import mc.project1.restapi.model.Post;
import mc.project1.restapi.service.PostService;
import org.hibernate.engine.internal.Collections;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PostController
{
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<PostDto> getPosts(@RequestParam(required = false) int page)
    {
        int pageNumber = page >= 0 ? page : 0;

        return postService.getPosts(pageNumber);
    }

    @GetMapping("/posts/comments")
    public List<Post> getPostsWithComments(@RequestParam(required = false) int page)
    {
        int pageNumber = page >= 0 ? page : 0;

        return postService.getPostsWithComments(pageNumber);
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id)
    {
        return postService.getPost(id);
    }
}
