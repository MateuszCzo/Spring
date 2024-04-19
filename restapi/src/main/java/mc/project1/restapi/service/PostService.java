package mc.project1.restapi.service;

import jakarta.transaction.Transactional;
import mc.project1.restapi.dto.PostDto;
import mc.project1.restapi.mapper.PostDtoMapper;
import mc.project1.restapi.model.Comment;
import mc.project1.restapi.model.Post;
import mc.project1.restapi.repository.CommentRepository;
import mc.project1.restapi.repository.PostRepository;
import org.hibernate.LazyInitializationException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService
{
    public static final int PAGE_SIZE = 20;
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
                        PAGE_SIZE,
                        Sort.by(sort, "id")
                )
        );

        return PostDtoMapper.mapToPostDtos(posts);
    }

    @Cacheable(cacheNames = "Post", key = "#id")
    public Post getPost(Long id)
    {
        return postRepository.findById(id)
                .orElseThrow();
    }

    @Cacheable(cacheNames = "PostsWithComments"/*, key = "#page"*/)
    public List<Post> getPostsWithComments(int pageNumber, Sort.Direction sort)
    {
        List<Post> posts = postRepository.findAllPosts(
                PageRequest.of(
                        pageNumber,
                        PAGE_SIZE,
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

    public Post addPost(Post post)
    {
        return postRepository.save(post);
    }

    @Transactional
    public Post editPost(Post post)
    {
        Post postEdited = postRepository.findById(post.getId())
                .orElseThrow();

        postEdited.setContent(post.getContent());
        postEdited.setTitle(post.getTitle());

        return postEdited;
    }

    @Transactional
    public void deletePost(Long id)
    {
        List<Comment> comments =  commentRepository.findAllByPostId(id);

        commentRepository.deleteAllInBatch(comments);

        postRepository.deleteById(id);
    }
}
