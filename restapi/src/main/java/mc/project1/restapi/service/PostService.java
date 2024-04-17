package mc.project1.restapi.service;

import mc.project1.restapi.dto.PostDto;
import mc.project1.restapi.mapper.PostDtoMapper;
import mc.project1.restapi.model.Comment;
import mc.project1.restapi.model.Post;
import mc.project1.restapi.repository.CommentRepository;
import mc.project1.restapi.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.invoke.CallSite;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService
{
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public List<PostDto> getPosts(int pageNumber, Sort.Direction sort)
    {
        List<Post> posts = postRepository.findAllPosts(
                PageRequest.of(
                        pageNumber,
                        20,
                        Sort.by(sort, "id")
                )
        );

        return PostDtoMapper.mapToPostDtos(posts);
    }

    public Post getPost(Long id)
    {
        return postRepository.findById(id)
                .orElseThrow();
    }

    public List<Post> getPostsWithComments(int pageNumber, Sort.Direction sort)
    {
        List<Post> posts = postRepository.findAllPosts(
                PageRequest.of(
                        pageNumber,
                        20,
                        Sort.by(sort, "id")
                )
        );

        List<Long> postsIds = posts.stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        List<Comment> comments = commentRepository.findAllByPostIdIn(postsIds);

        posts.forEach(post -> post.setComments(extractComments(comments, post.getId())));

        return posts;
    }

    public List<Comment> extractComments(List<Comment> comments, long postId)
    {
        return comments.stream()
                .filter(comment -> comment.getPostId() == postId)
                .collect(Collectors.toList());
    }
}
