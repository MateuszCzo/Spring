package mc.project1.restapi.service;

import lombok.RequiredArgsConstructor;
import mc.project1.restapi.dto.PostDto;
import mc.project1.restapi.mapper.PostDtoMapper;
import mc.project1.restapi.model.Post;
import mc.project1.restapi.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService
{
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostDto> getPosts(int page)
    {
        List<Post> posts = postRepository.findAllPosts(PageRequest.of(page, 20));

        return PostDtoMapper.mapToPostDtos(posts);
    }

    public Post getPost(Long id)
    {
        return postRepository.findById(id)
                .orElseThrow();
    }
}
