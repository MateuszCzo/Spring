package mc.project1.restapi.controller;

import mc.project1.restapi.dto.PostDto;
import mc.project1.restapi.model.Post;
import mc.project1.restapi.service.PostService;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController
{
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<PostDto> getPosts(@RequestParam(required = false) Integer page, Sort.Direction sort)
    {
        page = page != null && page >= 0 ? page : 0;
        sort = sort != null ? sort : Sort.Direction.ASC;

        return postService.getPosts(page, sort);
    }

    @GetMapping("/posts/comments")
    public List<Post> getPostsWithComments(@RequestParam(required = false) Integer page, Sort.Direction sort)
    {
        page = page != null && page >= 0 ? page : 0;
        sort = sort != null ? sort : Sort.Direction.ASC;

        return postService.getPostsWithComments(page, sort);
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id)
    {
        return postService.getPost(id);
    }

    @PostMapping("/posts")
    public Post addPost(@RequestBody Post post)
    {
        return postService.addPost(post);
    }

    @PutMapping("/posts")
    public Post editPost(@RequestBody Post post)
    {
        return postService.editPost(post);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id)
    {
        postService.deletePost(id);
    }
}
