package mc.project1.restapi.service;

import lombok.RequiredArgsConstructor;
import mc.project1.restapi.model.Post;
import mc.project1.restapi.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService
{
    private final PostRepository postRepository;

    public List<Post> getPosts()
    {
        return postRepository.findAll();
    }

    public Post getPost(Long id)
    {
        return postRepository.findById(id)
                .orElseThrow();
    }
}
